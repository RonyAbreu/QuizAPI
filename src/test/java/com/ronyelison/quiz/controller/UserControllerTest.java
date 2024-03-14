package com.ronyelison.quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.user.UserLogin;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.util.UserRequestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends QuizApplicationTests {
    MockUser mockUser = new MockUser();

    @Test
    void registerUser() {
        UserRequest userRequest = mockUser.mockDTO(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        assertNotNull(userResponse);
        assertNotNull(userResponse.uuid());
        assertNotNull(userResponse.email());
        assertNotNull(userResponse.name());
        assertNotNull(token);

        UserRequestUtil.delete(userResponse.uuid(), token);
    }

    @Test
    void loginUser() {
        UserRequest userRequest = mockUser.mockDTO(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);
    }

    @Test
    void removeUser() {
        String token = UserRequestUtil.login(new UserLogin("teste@gmail.com", "teste123"));
    }

    @Test
    void updateUser() {
    }
}