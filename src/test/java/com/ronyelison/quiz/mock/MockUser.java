package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.user.UserLogin;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserUpdate;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.entity.enums.Role;

import java.util.UUID;

public class MockUser implements MockInterface<User, UserRequest>{
    public static final String MOCK_TOKEN = "mockToken";

    @Override
    public User mockEntity(Integer num) {
        return new User(
                UUID.randomUUID(),
                "User" + num,
                "user@gmail.com",
                "12345678",
                Role.USER
        );
    }

    @Override
    public UserRequest mockRequest(Integer num) {
        return new UserRequest(
                "User" + num,
                "user@gmail.com",
                "12345678"
        );
    }

    public UserUpdate mockUserUpdate(){
        return new UserUpdate("Novo nome");
    }

    public UserLogin mockUserLogin(){
        return new UserLogin("user@gmail.com", "12345678");
    }
}
