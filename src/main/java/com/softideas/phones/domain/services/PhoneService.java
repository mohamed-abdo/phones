package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.models.UploadStats;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.stream.Stream;

public interface PhoneService extends PhoneNumberValidator {

    default String[] getSupportTypes() {
        return new String[]{"text/csv"};
    }

    Optional<UploadStats> importData(Stream<PhoneSheet> phoneSheetStream);

    Stream<PhoneSheet> parseInputStream(InputStream inputStream) throws IOException;

    NormalizedNumber tryToFixNumber(@NonNull String number);
}
