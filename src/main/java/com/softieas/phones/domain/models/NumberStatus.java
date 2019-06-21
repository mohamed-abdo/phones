package com.softieas.phones.domain.models;

public enum NumberStatus {
    VALID_NUMBER("VALID"),
    INVALID_NUMBER("INVALID"),
    FIXED_NUMBER("FIXED");
    private String code;

    NumberStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static NumberStatus fromString(String code) throws IllegalArgumentException {
        switch (code.toUpperCase()) {
            case "VALID":
                return NumberStatus.VALID_NUMBER;
            case "INVALID":
                return NumberStatus.INVALID_NUMBER;
            case "FIXED":
                return NumberStatus.FIXED_NUMBER;
            default:
                throw new IllegalArgumentException("code not exists");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
