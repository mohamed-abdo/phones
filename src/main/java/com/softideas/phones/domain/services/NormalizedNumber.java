package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.PhoneNumberStatus;
import com.softideas.phones.domain.models.RejectionReason;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Arrays;

@Data
public class NormalizedNumber {
    private final String number;
    private String fixedNumber;
    @Builder.Default
    private PhoneNumberStatus phoneNumberStatus = PhoneNumberStatus.INVALID_NUMBER;
    @Builder.Default
    private RejectionReason rejectionReason = RejectionReason.NOT_APPLICABLE;
    private PhoneNumberFixer phoneNumberFixer;

    final PhoneNumberValidator phoneNumberValidator;

    NormalizedNumber(@Autowired PhoneNumberValidator phoneNumberValidator, @NonNull final String number) {
        this.phoneNumberValidator = phoneNumberValidator;
        this.number = number;
        if (this.phoneNumberValidator.isValidCellNumber(this.number)) {
            this.fixedNumber = this.number;
            this.phoneNumberStatus = PhoneNumberStatus.VALID_NUMBER;
        } else {
            var fixEngine = Arrays.stream(PhoneNumberFixer.values())
                    .filter(f -> this.phoneNumberValidator.isValidCellNumber(f.fixNumber(this.number)))
                    .findAny()
                    .map(f -> {
                        this.fixedNumber = f.fixNumber(this.number);
                        this.phoneNumberFixer = f;
                        this.phoneNumberStatus = PhoneNumberStatus.FIXED_NUMBER;
                        return this;
                    });
            if (fixEngine.isEmpty()) {
                this.rejectionReason = this.phoneNumberValidator.inferRejectionReason(this.number);
            }
        }
    }


}
