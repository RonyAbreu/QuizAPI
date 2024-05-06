package br.ufpb.dcx.apps4society.quizapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScoreNotFoundException extends RuntimeException{
    public ScoreNotFoundException(String message) {
        super(message);
    }
}
