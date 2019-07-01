package com.softideas.phones.domain.models;

public enum PhoneNumberStatus {
    VALID("VALID"),
    INVALID("INVALID"),
    FIXED("FIXED");
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
