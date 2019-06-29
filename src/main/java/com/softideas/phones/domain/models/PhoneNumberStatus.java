package com.softideas.phones.domain.models;

public enum PhoneNumberStatus {
    VALID_NUMBER("VALID"),
    INVALID_NUMBER("MALFORMED"),
    FIXED_NUMBER("FIXED");
    private String code;

    PhoneNumberStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PhoneNumberStatus fromString(String code) throws IllegalArgumentException {
        switch (code.toUpperCase()) {
            case "VALID":
                return PhoneNumberStatus.VALID_NUMBER;
            case "MALFORMED":
                return PhoneNumberStatus.INVALID_NUMBER;
            case "FIXED":
                return PhoneNumberStatus.FIXED_NUMBER;
            default:
                throw new IllegalArgumentException("code not exists");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
