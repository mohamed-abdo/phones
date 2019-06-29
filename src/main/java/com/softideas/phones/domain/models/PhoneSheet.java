package com.softideas.phones.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class PhoneSheet implements Serializable {
    private String id;
    private String number;

    public static PhoneSheet fromEntry(Map.Entry<String, String> entry) {
        return new PhoneSheet(entry.getKey(), entry.getValue());
    }
}
