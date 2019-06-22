package com.softieas.phones.domain.models;

public enum PhoneStatus {
    VALID_NUMBER("VALID"),
    INVALID_NUMBER("INVALID"),
    FIXED_NUMBER("FIXED");
    private String code;

    PhoneStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PhoneStatus fromString(String code) throws IllegalArgumentException {
        switch (code.toUpperCase()) {
            case "VALID":
                return PhoneStatus.VALID_NUMBER;
            case "INVALID":
                return PhoneStatus.INVALID_NUMBER;
            case "FIXED":
                return PhoneStatus.FIXED_NUMBER;
            default:
                throw new IllegalArgumentException("code not exists");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
