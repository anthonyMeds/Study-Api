package com.example.demo.config.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity duplicateData (DataIntegrityViolationException exception) {

        ExceptionDto exceptionDto = new ExceptionDto("User already exists", "400");

        return ResponseEntity.badRequest().body(exceptionDto);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity dataNotFound (EntityNotFoundException exception) {

        ExceptionDto exceptionDto = new ExceptionDto("Data not found", "400");

        return ResponseEntity.badRequest().body(exceptionDto);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity generalException (Exception exception) {

        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "500");

        return ResponseEntity.internalServerError().body(exceptionDto);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity invalidArgument (MethodArgumentNotValidException exception) {

        String specificField = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getField()
                + ": " + exception.getBindingResult().getFieldError().getDefaultMessage();

        ExceptionDto exceptionDto = new ExceptionDto(specificField, "400");

        return ResponseEntity.internalServerError().body(exceptionDto);

    }


}
