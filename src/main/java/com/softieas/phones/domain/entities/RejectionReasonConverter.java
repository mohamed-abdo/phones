package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.RejectionReason;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RejectionReasonConverter implements AttributeConverter<RejectionReason,String> {
    @Override
    public String convertToDatabaseColumn(RejectionReason rejectionReason) {
        return rejectionReason.getCode();
    }

    @Override
    public RejectionReason convertToEntityAttribute(String code) {
        return RejectionReason.fromString(code);
    }
}
