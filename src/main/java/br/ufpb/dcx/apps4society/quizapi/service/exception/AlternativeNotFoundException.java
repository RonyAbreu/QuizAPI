package br.ufpb.dcx.apps4society.quizapi.service.exception;

public class AlternativeNotFoundException extends RuntimeException{
    public AlternativeNotFoundException(String message) {
        super(message);
    }
}
