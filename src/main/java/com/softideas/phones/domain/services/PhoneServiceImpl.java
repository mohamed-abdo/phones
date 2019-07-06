package com.softideas.phones.domain.services;

import com.opencsv.CSVReader;
import com.softideas.phones.domain.entities.ImportedFile;
import com.softideas.phones.domain.entities.Phone;
import com.softideas.phones.domain.models.PhoneNumberStatus;
import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.models.UploadStats;
import com.softideas.phones.domain.repositories.ImportedFileRepository;
import com.softideas.phones.domain.repositories.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PhoneServiceImpl extends PhoneNumberSouthAfricaValidatorImpl implements PhoneService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PhoneServiceImpl.class);

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ImportedFileRepository importedFileRepository;

    @Override
    public Optional<UploadStats> getImportedFileStats(@NonNull UUID fileId) {
        return importedFileRepository.findById(fileId)
                .map(importedFile ->
                        calcStats(importedFile.getFileRef(), false, importedFile.getPhones()));
    }

    @Transactional
    @Override
    public Optional<UploadStats> importData(@NonNull String fileChecksum, @NonNull Stream<PhoneSheet> phoneSheetStream) {
        LOGGER.debug("start importing phone data");
        AtomicBoolean isNewFile = new AtomicBoolean(false);
        ImportedFile importedFile = importedFileRepository.findByChecksum(fileChecksum)
                .orElseGet(() -> {
                    isNewFile.set(true);
                    var newFile = new ImportedFile();
                    newFile.setChecksum(fileChecksum);
                    return newFile;
                });
        LOGGER.debug("file is new:{} ", isNewFile.get());
        var phoneList = phoneSheetStream
                .map(this::tryToFixNumber)
                .map(n -> this.mapPhone(n, importedFile))
                .collect(Collectors.toSet());

        importedFile.setPhones(phoneList);
        importedFileRepository.save(importedFile);
        var stats = calcStats(importedFile.getFileRef(), isNewFile.get(), importedFile.getPhones());
        LOGGER.debug("saving file, status:{} ", stats);
        return Optional.of(stats);
    }

    private UploadStats calcStats(UUID uuid, boolean isNewFile, Set<Phone> phoneList) {
        var phoneGroups = phoneList.stream().collect(Collectors.groupingBy(Phone::getStatus));
        var validNumbers = phoneGroups.getOrDefault(PhoneNumberStatus.VALID, Collections.EMPTY_LIST).size();
        var fixedNumbers = phoneGroups.getOrDefault(PhoneNumberStatus.FIXED, Collections.EMPTY_LIST).size();
        var inValidNumbers = phoneGroups.getOrDefault(PhoneNumberStatus.INVALID, Collections.EMPTY_LIST).size();
        return new UploadStats(uuid, validNumbers, fixedNumbers, inValidNumbers, isNewFile, LocalDateTime.now());
    }

    private Phone mapPhone(NormalizedNumber normalizedNumber, ImportedFile importedFile) {
        Phone phone = phoneRepository.findById(normalizedNumber.getNumberId())
                .orElseGet(() -> this.getNewPhoneObj(normalizedNumber, importedFile));
        phone.setNumber(Optional.ofNullable(normalizedNumber.getFixedNumber()).orElse(normalizedNumber.getNumber()));
        phone.setOriginalNumber(normalizedNumber.getNumber());
        phone.setStatus(normalizedNumber.getPhoneNumberStatus());
        phone.setRejection(normalizedNumber.getRejectionReason());
        return phone;
    }

    private Phone getNewPhoneObj(NormalizedNumber normalizedNumber, ImportedFile importedFile) {
        Phone phone = new Phone();
        phone.setImportedFile(importedFile);
        phone.setId(normalizedNumber.getNumberId());
        return phone;
    }

    @Override
    public Stream<PhoneSheet> parseInputStream(@NonNull InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);
        try (var csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            return csvReader
                    .readAll()
                    .stream()
                    .filter(i -> i.length >= 2)
                    .map(i -> new PhoneSheet(i[0], i[1]));
        }
    }

    @Override
    public NormalizedNumber tryToFixNumber(@NonNull PhoneSheet phoneSheet) {
        Objects.requireNonNull(phoneSheet);
        return new NormalizedNumber(this, phoneSheet);
    }
}
