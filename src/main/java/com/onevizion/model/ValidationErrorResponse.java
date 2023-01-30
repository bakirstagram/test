package com.onevizion.model;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ValidationErrorResponse {

    private final HttpStatus status;
    private final String message;
    private final Map<String, String> validationErrors;

    public ValidationErrorResponse(HttpStatus status, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
