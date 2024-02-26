package com.ronyelison.quiz.service.exception;

public class AlternativeNotFoundException extends RuntimeException{
    public AlternativeNotFoundException(String message) {
        super(message);
    }
}
