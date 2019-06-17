package com.softieas.phones.domain.entities;

import com.softieas.phones.domain.models.ImportStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ImportStatusConverter implements AttributeConverter<ImportStatus, String> {
    @Override
    public String convertToDatabaseColumn(ImportStatus importStatus) {
        return importStatus.getCode();
    }

    @Override
    public ImportStatus convertToEntityAttribute(String status) {
        return ImportStatus.fromString(status);
    }
}
