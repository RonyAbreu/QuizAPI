package com.ronyelison.quiz.service.exception;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String message) {
        super(message);
    }
}
