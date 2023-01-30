package com.onevizion.exception;

import com.onevizion.model.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

import static com.onevizion.util.Constants.VALIDATION_ERROR;
import static com.onevizion.util.Constants.VALIDATION_VALUE_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class BooksExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<ValidationErrorResponse> handleRecordNotFoundException(ValidationException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> {
                            String defaultMessage = fieldError.getDefaultMessage();
                            return defaultMessage == null ? VALIDATION_VALUE_ERROR : defaultMessage;
                        }
                ));

        ValidationErrorResponse validationResult = new ValidationErrorResponse(
                BAD_REQUEST,
                VALIDATION_ERROR,
                validationErrors
        );

        return ResponseEntity.badRequest().body(validationResult);
    }

}
