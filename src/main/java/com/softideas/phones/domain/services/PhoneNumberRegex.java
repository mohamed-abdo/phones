package com.softideas.phones.domain.services;

import java.util.regex.Pattern;

public enum PhoneNumberRegex {
    VALID_CELL_NUMBER("^(\\+?27|0)[6-8][0-9]{8}$"),
    LEADING_ZEROS("^0+(?!$)"),
    INTERNATIONAL_WITH_LEADING_ZEROS("^(\\+?270)+(?!$)"),
    NON_DIGITS("[^\\d]+");

    String regex;

    PhoneNumberRegex(final String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public Pattern getPattern(){
        return Pattern.compile(this.regex);
    }
}
