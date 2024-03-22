package br.ufpb.dcx.apps4society.quizapi.mock;

public interface MockInterface<T,E> {
    T mockEntity(Integer num);
    E mockRequest(Integer num);
}
