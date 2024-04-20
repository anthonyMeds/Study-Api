package com.example.demo.config.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity duplicateData (DataIntegrityViolationException exception) {

        ExceptionDto exceptionDto = new ExceptionDto("User already exists", "400");

        return ResponseEntity.badRequest().body(exceptionDto);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> dataNotFound (EntityNotFoundException exception) {

        ExceptionDto exceptionDto = new ExceptionDto("Data not found", "400");

        return ResponseEntity.badRequest().body(exceptionDto);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> generalException (Exception exception) {

        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "500");

        return ResponseEntity.internalServerError().body(exceptionDto);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> invalidArgument (MethodArgumentNotValidException exception) {

        String specificField = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getField()
                + ": " + exception.getBindingResult().getFieldError().getDefaultMessage();

        ExceptionDto exceptionDto = new ExceptionDto(specificField, "400");

        return ResponseEntity.internalServerError().body(exceptionDto);

    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ExceptionDto> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        ExceptionDto exceptionDto = new ExceptionDto(ex.getMessage(), "400");
        return ResponseEntity.internalServerError().body(exceptionDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> httpNotReadable(HttpMessageNotReadableException ex) {
        ExceptionDto exceptionDto = new ExceptionDto("Invalid parameters. The request should have valid arguments", "400");
        return ResponseEntity.internalServerError().body(exceptionDto);
    }




}
