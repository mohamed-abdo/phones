package com.softideas.phones.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
class ApiError {
    @Builder.Default
    private UUID debugId = UUID.randomUUID();
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.DATE_TIME_PATTERN)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    @Builder.Default
    private String message = "Unexpected error";
    @Getter(AccessLevel.NONE)
    private String debugMessage;
    @Setter
    private Set<ApiValidationError> moreDetails;

    ApiError(HttpStatus status) {
        this.status = status;
    }

    ApiError(HttpStatus status, String message, Exception ex) {

        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    ResponseEntity<Object> buildResponseEntity() {
        return new ResponseEntity<>(this, this.getStatus());
    }

}