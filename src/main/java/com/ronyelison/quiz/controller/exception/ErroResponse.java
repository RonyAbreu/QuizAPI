package com.ronyelison.quiz.controller.exception;

import java.time.Instant;

public class ErroResponse {
    private Instant timestamp;
    private Integer code;
    private String message;
    private String path;

    public ErroResponse(Instant timestamp, Integer code, String message, String path) {
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
