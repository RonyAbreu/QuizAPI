package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.user.*;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.ronyelison.quiz.util.UserRequestUtil.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends QuizApplicationTests {
    MockUser mockUser = new MockUser();

    @Test
    void registerUserByNameEmailPassword_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(UserResponse.class);

        assertNotNull(userResponse);
        assertNotNull(userResponse.uuid());
        assertNotNull(userResponse.name());
        assertNotNull(userResponse.email());

        UserLogin userLogin = mockUser.mockUserLogin();
        String token = UserRequestUtil.login(userLogin);
        UserRequestUtil.delete(userResponse.uuid(), token);
    }

    @Test
    void loginUserByEmailPassword_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_LOGIN)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenResponse.class)
                .token();

        assertNotNull(token);
        assertTrue(token.contains("."));
    }

    @Test
    void removeUserByUuidAndToken_shouldReturn204Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        given()
                .header("Authorization", "Bearer "+token)
                .delete(baseURI+":"+port+basePath+BASE_PATH_USER+userResponse.uuid())
                .then()
                .statusCode(204)
                .extract()
                .response();
    }

    @Test
    void updateUserByNameAndToken_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        UserResponse response = given()
                .header("Authorization", "Bearer "+token)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI+":"+port+basePath+BASE_PATH_USER+userResponse.uuid())
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);

        assertNotNull(response);
        assertNotNull(response.uuid());
        assertNotNull(response.email());
        assertNotNull(response.name());
        assertEquals("Novo nome", response.name());

        UserRequestUtil.delete(userResponse.uuid(), token);
    }
}