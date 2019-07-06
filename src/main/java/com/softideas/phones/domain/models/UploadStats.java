package com.softideas.phones.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadStats implements Serializable {
    private UUID fileRef;
    private int validNumbers;
    private int fixedNumbers;
    private int invalidNumbers;
    @JsonIgnore
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    private boolean isNewFile;
    private LocalDateTime createdOn;
}
