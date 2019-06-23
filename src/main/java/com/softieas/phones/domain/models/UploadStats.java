package com.softieas.phones.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class UploadStats implements Serializable {
    private UUID fileRef;
    private int validNumbers;
    private int fixedNumbers;
    private int invalidNumbers;
    private LocalDateTime createdOn;
}
