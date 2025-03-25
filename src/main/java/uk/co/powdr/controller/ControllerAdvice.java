package uk.co.powdr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uk.co.powdr.dto.ApiError;
import uk.co.powdr.exception.AccessLevelException;
import uk.co.powdr.exception.IncorrectPasswordException;
import uk.co.powdr.exception.ResourceNotFoundException;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError().status(HttpStatus.NOT_FOUND).message(ex.getMessage());
        log.warn("Handler for ResourceNotFoundException triggered with message: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(AccessLevelException.class)
    public ResponseEntity<ApiError> handleAccessLevel(AccessLevelException ex) {
        ApiError apiError = new ApiError().status(HttpStatus.UNAUTHORIZED).message(ex.getMessage());
        log.warn("Handler for AccessLevelException triggered with message: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiError> handleIncorrectPassword(IncorrectPasswordException ex) {
        ApiError apiError = new ApiError().status(HttpStatus.UNAUTHORIZED).message(ex.getMessage());
        log.warn("Handler for IncorrectPasswordException triggered with message: {}", ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
