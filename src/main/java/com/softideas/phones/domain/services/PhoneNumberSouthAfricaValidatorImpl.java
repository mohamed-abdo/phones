package com.softideas.phones.domain.services;

import com.softideas.phones.models.RejectionReason;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class PhoneNumberSouthAfricaValidatorImpl implements PhoneNumberValidator {

    @Override
    public Pattern getCellRegexPattern() {
        return PhoneNumberRegex.VALID_CELL_NUMBER.getPattern();
    }

    @Override
    public String normalizeValidNumber(String number) throws IllegalArgumentException {
        Objects.requireNonNull(number);
        if (!isValidCellNumber(number))
            throw new IllegalArgumentException(String.format("number %s is not a valid number", number));
        return number
                .replaceFirst(PhoneNumberRegex.INTERNATIONAL_WITH_LEADING_ZEROS.getRegex(), "27");
    }

    @Override
    public RejectionReason inferRejectionReason(@NonNull String number) {
        Objects.requireNonNull(number);
        if (number.length() < 8)
            return RejectionReason.MISSING_DIGITS;
        else if (PhoneNumberRegex.NON_DIGITS.getPattern().matcher(number).find())
            return RejectionReason.ILLEGAL_CHARS;
        else if (number.length() > 12)
            return RejectionReason.TOO_MANY_DIGITS;
        else
            return RejectionReason.MALFORMED;

    }
}
