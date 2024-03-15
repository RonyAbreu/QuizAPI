package com.ronyelison.quiz.mock;

public interface MockInterface<T,E> {
    T mockEntity(Integer num);
    E mockRequest(Integer num);
}
