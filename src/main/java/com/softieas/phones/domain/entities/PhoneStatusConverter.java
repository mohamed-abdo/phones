package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.PhoneStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PhoneStatusConverter implements AttributeConverter<PhoneStatus, String> {
    @Override
    public String convertToDatabaseColumn(PhoneStatus phoneStatus) {
        return phoneStatus.getCode();
    }

    @Override
    public PhoneStatus convertToEntityAttribute(String status) {
        return PhoneStatus.fromString(status);
    }
}
