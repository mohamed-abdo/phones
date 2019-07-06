package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.models.UploadStats;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface PhoneService extends PhoneNumberValidator {

    default String[] getSupportTypes() {
        return new String[]{"text/csv"};
    }

    Optional<UploadStats> importData(@NonNull String fileChecksum,@NonNull Stream<PhoneSheet> phoneSheetStream);

    Stream<PhoneSheet> parseInputStream(@NonNull InputStream inputStream) throws IOException;

    NormalizedNumber tryToFixNumber(@NonNull PhoneSheet phoneSheet);

    Optional<UploadStats> getImportedFileStats(@NonNull UUID fileId);
}
