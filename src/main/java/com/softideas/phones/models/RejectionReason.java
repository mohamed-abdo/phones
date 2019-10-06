package com.softideas.phones.models;

public enum RejectionReason {
    ILLEGAL_CHARS("ILLEGAL_CHARS", "number contains illegal alphanumeric characters."),
    MALFORMED("MALFORMED", "number is not compliant with business rules."),
    MISSING_DIGITS("MISSING_DIGITS", "missing digits."),
    TOO_MANY_DIGITS("TOO_MANY_DIGITS", "too many digits."),
    NOT_APPLICABLE("NOT_APPLICABLE", "no rejection."),
    UNKNOWN("UNKNOWN", "unknown reason.");

    private final String description;
    private final String code;

    RejectionReason(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RejectionReason fromString(String code) throws IllegalArgumentException {
        return RejectionReason.valueOf(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
