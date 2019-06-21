package com.softieas.phones.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class UploadStats {
    private UUID fileRef;
    private int validNumbers;
    private int fixedNumbers;
    private int invalidNumbers;
    private LocalDateTime createdOn;
}
