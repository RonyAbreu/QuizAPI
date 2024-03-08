package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.user.UserLogin;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserUpdate;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.entity.enums.Role;

import java.util.UUID;

public class MockUser implements MockInterface<User, UserRequest>{
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
    public UserRequest mockDTO(Integer num) {
        return new UserRequest(
                "User" + num,
                "user@gmail.com",
                "12345678"
        );
    }

    public UserLogin mockUserLogin(String email, String password){
        return new UserLogin(email, password);
    }

    public UserUpdate mockUserUpdate(){
        return new UserUpdate("Novo nome");
    }
}
