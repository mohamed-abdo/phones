package com.softieas.phones.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PhoneSheet implements Serializable {
    private String id;
    private String number;
}
