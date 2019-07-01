package com.softideas.phones.domain.services;

import com.softideas.phones.domain.models.PhoneNumberStatus;
import com.softideas.phones.domain.models.PhoneSheet;
import com.softideas.phones.domain.models.RejectionReason;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Arrays;

@Getter
public class NormalizedNumber {
    private final static Logger LOGGER = LoggerFactory.getLogger(NormalizedNumber.class);

    @Builder.Default
    private long numberId = 999999999;//invalid imported id
    private final String number;
    private String fixedNumber;
    @Builder.Default
    private PhoneNumberStatus phoneNumberStatus = PhoneNumberStatus.INVALID;
    @Builder.Default
    private RejectionReason rejectionReason = RejectionReason.NOT_APPLICABLE;
    private PhoneNumberFixer phoneNumberFixer;

    private final PhoneNumberValidator phoneNumberValidator;

    NormalizedNumber(PhoneNumberValidator phoneNumberValidator, @NonNull final PhoneSheet phoneSheet) {
        try {
            this.numberId = Long.valueOf(phoneSheet.getId());
        } catch (NumberFormatException e) {
            LOGGER.error("invalid phone number id format, id:{}, number:{}", phoneSheet.getId(), phoneSheet.getNumber());
        }

        this.phoneNumberValidator = phoneNumberValidator;
        this.number = phoneSheet.getNumber();
        if (this.phoneNumberValidator.isValidCellNumber(this.number)) {
            this.fixedNumber = this.number;
            this.phoneNumberStatus = PhoneNumberStatus.VALID;
        } else {
            var fixEngine = Arrays.stream(PhoneNumberFixer.values())
                    .filter(f -> this.phoneNumberValidator.isValidCellNumber(f.fixNumber(this.number)))
                    .findAny()
                    .map(f -> {
                        this.fixedNumber = f.fixNumber(this.number);
                        this.phoneNumberFixer = f;
                        this.phoneNumberStatus = PhoneNumberStatus.FIXED;
                        return this;
                    });
            if (fixEngine.isEmpty()) {
                this.rejectionReason = this.phoneNumberValidator.inferRejectionReason(this.number);
            }
        }
    }


}
