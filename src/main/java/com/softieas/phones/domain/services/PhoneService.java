package com.softieas.phones.domain.services;

import com.softieas.phones.domain.models.PhoneSheet;
import com.softieas.phones.domain.models.UploadStats;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

public interface PhoneService {

    default String[] getSupportTypes() {
        return new String[]{"text/csv"};
    }

    UploadStats importData(Stream<PhoneSheet> phoneSheetStream);

    Stream<PhoneSheet> parseInputStream(InputStream inputStream) throws IOException;
}
