package com.softideas.phones.domain.services;


import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.function.Function;

public enum PhoneNumberFixer {

    ADD_PREFIX_ZERO(number -> {
        Objects.requireNonNull(number);
        return "0" + number;
    }),

    REMOVE_LEADING_ZEROS(number -> {
        Objects.requireNonNull(number);
        return number.replaceFirst(PhoneNumberRegex.LEADING_ZEROS.getRegex(), "");
    }),

    REMOVE_NON_DIGITS(number -> {
        Objects.requireNonNull(number);
        return number.replaceAll(PhoneNumberRegex.NON_DIGITS.getRegex(),"");
    }),

    REMOVE_INTERNATIONAL_WITH_LEADING_ZEROS(number -> {
        Objects.requireNonNull(number);
        return number.replaceFirst(PhoneNumberRegex.INTERNATIONAL_WITH_LEADING_ZEROS.getRegex(), "27");
    });

    final Function<String, String> fixer;

    PhoneNumberFixer(Function<String, String> fixer) {
        this.fixer = fixer;
    }

    String fixNumber(@NonNull String number) {
        Objects.requireNonNull(number);
        return this.fixer.apply(number);
    }
}
