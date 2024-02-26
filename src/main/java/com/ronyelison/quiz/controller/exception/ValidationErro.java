package com.ronyelison.quiz.controller.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ValidationErro extends ErroResponse{
    private List<FieldMessage> fieldMessageList = new ArrayList<>();

    public ValidationErro(Instant timestamp, Integer code, String message, String path) {
        super(timestamp, code, message, path);
    }

    public void addErro(String field, String message){
        this.fieldMessageList.add(new FieldMessage(field, message));
    }

    public List<FieldMessage> getFieldMessageList() {
        return fieldMessageList;
    }
}
