package br.ufpb.dcx.apps4society.quizapi.service.exception;

public class TokenException extends RuntimeException{
    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
