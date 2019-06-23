package com.softieas.phones.domain.services;

import com.opencsv.CSVReader;
import com.softieas.phones.domain.models.PhoneSheet;
import com.softieas.phones.domain.models.UploadStats;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Override
    public Optional<UploadStats> importData(@NonNull Stream<PhoneSheet> phoneSheetStream) {
        return Optional.empty();
    }

    @Override
    public Stream<PhoneSheet> parseInputStream(@NonNull InputStream inputStream) throws IOException {
        try (var csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            return csvReader
                    .readAll()
                    .stream()
                    .filter(i -> i.length >= 2)
                    .map(i -> new PhoneSheet(i[0], i[1]));
        }
    }
}
