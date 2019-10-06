package com.softideas.phones.domain.entities;

import com.softideas.phones.models.PhoneNumberStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PhoneStatusConverter implements AttributeConverter<PhoneNumberStatus, String> {
    @Override
    public String convertToDatabaseColumn(PhoneNumberStatus phoneNumberStatus) {
        return phoneNumberStatus.getCode();
    }

    @Override
    public PhoneNumberStatus convertToEntityAttribute(String status) {
        return PhoneNumberStatus.fromString(status);
    }
}
