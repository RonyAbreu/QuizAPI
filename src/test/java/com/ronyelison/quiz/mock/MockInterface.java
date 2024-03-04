package com.ronyelison.quiz.mock;

public interface MockInterface<T,E> {
    T mockEntity(Integer num);
    E mockDTO(Integer num);
}
