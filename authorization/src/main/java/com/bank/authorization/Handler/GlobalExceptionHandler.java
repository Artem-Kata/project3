package com.bank.authorization.Handler;

import com.bank.authorization.Exceptions.AuthenticationException;
import com.bank.authorization.Exceptions.EntityNotFoundException;
import org.hibernate.exception.DataException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        final Map<String, Object> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        final Map<String, Object> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        final Map<String, Object> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<?> handleDataException(DataException ex) {
        final Map<String, Object> error = new HashMap<>();
        error.put("error", "Database error: " + ex.getCause().getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(DataAccessException ex) {
        final Map<String, Object> error = new HashMap<>();
        error.put("error", "Database error: " + NestedExceptionUtils.getMostSpecificCause(ex).getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
