package com.softieas.phones.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
class ApiValidationError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

}