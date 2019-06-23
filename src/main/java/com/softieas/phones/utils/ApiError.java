package com.softieas.phones.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
class ApiError {
    @Builder.Default
    private UUID debugId = UUID.randomUUID();
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_TIME_PATTERN)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    @Builder.Default
    private String message = "Unexpected error";
    private String debugMessage;
    private Set<ApiValidationError> moreDetails;

    ApiError(HttpStatus status) {
        this.status = status;
    }

    ApiError(HttpStatus status, String message, Throwable ex) {

        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ResponseEntity<Object> buildResponseEntity() {
        return new ResponseEntity<>(this, this.getStatus());
    }

}