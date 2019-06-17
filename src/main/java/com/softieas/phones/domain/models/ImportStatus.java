package com.softieas.phones.domain.models;

public enum ImportStatus {
    VALID_NUMBER("VALID"),
    INVALID_NUMBER("INVALID"),
    FIXED_NUMBER("FIXED");
    private String code;

    ImportStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ImportStatus fromString(String code) throws IllegalArgumentException {
        switch (code.toUpperCase()) {
            case "VALID":
                return ImportStatus.VALID_NUMBER;
            case "INVALID":
                return ImportStatus.INVALID_NUMBER;
            case "FIXED":
                return ImportStatus.FIXED_NUMBER;
            default:
                throw new IllegalArgumentException("code not exists");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
