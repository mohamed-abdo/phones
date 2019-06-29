package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.RejectionReason;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.regex.Pattern;

public interface PhoneNumberValidator {
    Pattern getCellRegexPattern();

    String normalizeValidNumber(@NonNull String number) throws IllegalArgumentException;

    RejectionReason inferRejectionReason(@NonNull String number);

    default boolean isValidCellNumber(@NonNull String number) {
        Objects.requireNonNull(number);
        return getCellRegexPattern().matcher(number).find();
    }
}
