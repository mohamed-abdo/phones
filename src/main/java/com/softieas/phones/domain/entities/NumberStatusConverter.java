package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.NumberStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class NumberStatusConverter implements AttributeConverter<NumberStatus, String> {
    @Override
    public String convertToDatabaseColumn(NumberStatus numberStatus) {
        return numberStatus.getCode();
    }

    @Override
    public NumberStatus convertToEntityAttribute(String status) {
        return NumberStatus.fromString(status);
    }
}
