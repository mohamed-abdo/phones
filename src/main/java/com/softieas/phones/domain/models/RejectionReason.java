package com.softieas.phones.domain.models;

public enum RejectionReason {
    ILLEGAL_CHARS("CHARS"),
    MALFORMED("FORMAT"),
    UNKNOWN("UNKNOWN");
    private String code;

    RejectionReason(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static RejectionReason fromString(String code) throws IllegalArgumentException {
        switch (code.toUpperCase()) {
            case "CHARS":
                return RejectionReason.ILLEGAL_CHARS;
            case "FORMAT":
                return RejectionReason.MALFORMED;
            case "UNKNOWN":
                return RejectionReason.UNKNOWN;
            default:
                throw new IllegalArgumentException("code not exists");
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
