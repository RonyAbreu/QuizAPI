package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.entity.enums.Role;

import java.util.UUID;

public class MockUser implements MockInterface<User, UserRequest>{
    @Override
    public User mockEntity(Integer num) {
        return new User(
                UUID.randomUUID(),
                "User",
                "user@gmail.com",
                "12345678",
                Role.USER
        );
    }

    @Override
    public UserRequest mockDTO(Integer num) {
        return new UserRequest(
                "User",
                "user@gmail.com",
                "12345678"
        );
    }
}
