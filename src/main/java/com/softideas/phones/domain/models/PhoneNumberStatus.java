package com.softideas.phones.domain.models;

public enum PhoneNumberStatus {
    VALID_NUMBER("VALID"),
    INVALID_NUMBER("INVALID"),
    FIXED_NUMBER("FIXED");
    private String code;

    PhoneNumberStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PhoneNumberStatus fromString(String code) throws IllegalArgumentException {
        return PhoneNumberStatus.valueOf(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
