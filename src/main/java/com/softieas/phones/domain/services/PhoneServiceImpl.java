package com.softieas.phones.domain.services;

import com.opencsv.CSVReader;
import com.softieas.phones.domain.models.PhoneSheet;
import com.softieas.phones.domain.models.UploadStats;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Service
public class PhoneServiceImpl implements PhoneService {
    @Override
    public UploadStats importData(Stream<PhoneSheet> phoneSheetStream) {
        return null;
    }

    @Override
    public Stream<PhoneSheet> parseInputStream(InputStream inputStream) throws IOException {
        try (var csvReader = new CSVReader(new InputStreamReader(inputStream))) {
            var fileContent = csvReader.readAll();
            return fileContent.stream().map(i -> new PhoneSheet(Long.valueOf(i[0]), i[1]));
        }
    }
}
