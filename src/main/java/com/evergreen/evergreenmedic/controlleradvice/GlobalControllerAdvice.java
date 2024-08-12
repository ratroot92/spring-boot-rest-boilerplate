package com.evergreen.evergreenmedic.controlleradvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        HashMap<String, String> errResponse = new HashMap<>();
        errResponse.put("exception", ex.getClass().getName());
        errResponse.put("status", HttpStatus.NOT_FOUND.name());
        errResponse.put("error", ex.getMessage());
        errResponse.put("statusCode", HttpStatus.NOT_FOUND.toString());
        errResponse.put("stackTrace", ex.getStackTrace().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(Exception ex, WebRequest request) {
        HashMap<String, String> errResponse = new HashMap<>();
        errResponse.put("exception", ex.getClass().getName());
        errResponse.put("status", HttpStatus.UNAUTHORIZED.name());
        errResponse.put("error", ex.getMessage());
        errResponse.put("statusCode", HttpStatus.UNAUTHORIZED.toString());
        errResponse.put("stackTrace", ex.getStackTrace().toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        HashMap<String, String> errResponse = new HashMap<>();
        errResponse.put("exception", ex.getClass().getName());
        errResponse.put("status", HttpStatus.BAD_REQUEST.name());
        errResponse.put("error", ex.getMessage());
        errResponse.put("statusCode", HttpStatus.BAD_REQUEST.toString());
        errResponse.put("stackTrace", ex.getStackTrace().toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        HashMap<String, String> errResponse = new HashMap<>();
        errResponse.put("exception", ex.getClass().getName());
        errResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.name());
        errResponse.put("error", ex.getMessage());
        errResponse.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errResponse.put("stackTrace", ex.getStackTrace().toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errResponse);
    }
}
