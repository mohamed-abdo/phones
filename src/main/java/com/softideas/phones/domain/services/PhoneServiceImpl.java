package com.softideas.phones.domain.services;

import com.opencsv.CSVReader;
import com.softideas.phones.domain.entities.ImportedFile;
import com.softideas.phones.domain.entities.Phone;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PhoneServiceImpl extends PhoneNumberSouthAfricaValidatorImpl implements PhoneService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PhoneServiceImpl.class);

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ImportedFileRepository importedFileRepository;

    @Transactional
    @Override
    public Optional<UploadStats> importData(@NonNull Stream<PhoneSheet> phoneSheetStream) {
        ImportedFile importedFile = new ImportedFile();
        var uuid = UUID.randomUUID();
        importedFile.setFileRef(uuid);
        var phoneList = phoneSheetStream
                .map(this::tryToFixNumber)
                .map(n -> this.mapPhone(n, importedFile))
                .collect(Collectors.toList());
        importedFile.setPhones(phoneList);
        importedFileRepository.save(importedFile);
        return Optional.empty();
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
