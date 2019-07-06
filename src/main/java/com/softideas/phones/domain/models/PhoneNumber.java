package com.softideas.phones.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumber implements Serializable {
    private String number;
    private String fixedNumber;
    @Builder.Default
    private PhoneNumberStatus phoneNumberStatus = PhoneNumberStatus.INVALID;
    @Builder.Default
    private RejectionReason rejectionReason = RejectionReason.NOT_APPLICABLE;
    private String fixer;
}
