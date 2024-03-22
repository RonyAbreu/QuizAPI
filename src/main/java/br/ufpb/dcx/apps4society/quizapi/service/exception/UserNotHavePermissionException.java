package br.ufpb.dcx.apps4society.quizapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotHavePermissionException extends Exception{
    public UserNotHavePermissionException(String message) {
        super(message);
    }
}
