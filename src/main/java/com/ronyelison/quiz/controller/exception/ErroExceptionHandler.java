package com.ronyelison.quiz.controller.exception;

import com.ronyelison.quiz.service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ErroExceptionHandler {

    @ExceptionHandler(AlternativeCorrectDuplicateException.class)
    public ResponseEntity<ErroResponse> alternativeCorrectDuplicateErro(AlternativeCorrectDuplicateException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(FalseAlternativesOnlyException.class)
    public ResponseEntity<ErroResponse> falseAlternativesOnlyErro(FalseAlternativesOnlyException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(LimitOfAlternativesException.class)
    public ResponseEntity<ErroResponse> limitOfAlternativesErro(LimitOfAlternativesException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ErroResponse> questionNotFoundErro(QuestionNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ErroResponse> themeNotFoundErro(ThemeNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validationErro(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationErro validationErro = new ValidationErro(Instant.now(), status.value(), "Validation erro", request.getRequestURI());
        for (FieldError error: e.getFieldErrors()){
            validationErro.addErro(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(validationErro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> dataIntegrityErro(DataIntegrityViolationException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroResponse erroResponse = new ErroResponse(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status).body(erroResponse);
    }
}
