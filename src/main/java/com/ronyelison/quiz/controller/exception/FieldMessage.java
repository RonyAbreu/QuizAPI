package com.ronyelison.quiz.controller.exception;

public class FieldMessage {
    private String field;
    private String message;

    public FieldMessage(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
