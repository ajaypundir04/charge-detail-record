package com.dcs.cdr.charge.detail.record.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Stream;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, Stream.concat(exception.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()), exception.getBindingResult().getGlobalErrors().stream().map(error -> error.getObjectName() + ": " + error.getDefaultMessage())).toList().toString()).build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {

        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getLocalizedMessage()).build();
    }

    @ExceptionHandler(CdrException.class)
    public ErrorResponse handleCdrException(CdrException exception) {

        return ErrorResponse.builder(exception, exception.getStatusCode(), exception.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exception) {

        return ErrorResponse.builder(exception, HttpStatusCode.valueOf(500), exception.getMessage()).build();
    }


}