package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.user.*;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.ronyelison.quiz.util.UserRequestUtil.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends QuizApplicationTests {

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
    void registerUserByEmailAlreadyExists_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(400);

        UserLogin userLogin = mockUser.mockUserLogin();
        String token = UserRequestUtil.login(userLogin);
        UserRequestUtil.delete(userResponse.uuid(), token);
    }

    @Test
    void registerUserByNameInvalid_shouldReturn400Test() {
        UserRequest userRequest = new UserRequest("", "user@gmail.com", "12345678");

        given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(400);
    }

    @Test
    void registerUserByEmailInvalid_shouldReturn400Test() {
        UserRequest userRequest = new UserRequest("rony", "", "12345678");

        given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(400);
    }

    @Test
    void registerUserByPasswordInvalid_shouldReturn400Test() {
        UserRequest userRequest = new UserRequest("rony", "user@gmail.com", "");

        given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(400);
    }

    @Test
    void registerUserByPasswordLessThen8Characters_shouldReturn400Test() {
        UserRequest userRequest = new UserRequest("rony", "user@gmail.com", "1234567");

        given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_REGISTER)
                .then()
                .statusCode(400);
    }

    @Test
    void loginUserByEmailPassword_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

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

        UserRequestUtil.delete(userResponse.uuid(), token);
    }

    @Test
    void loginUserDoesNotExist_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = new UserLogin("nonexistent@gmail.com", "nonexistent");

        given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_LOGIN)
                .then()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void loginUserByEmailInvalid_shouldReturn400Test() {
        UserLogin userLogin = new UserLogin("", "12345678");

        given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_LOGIN)
                .then()
                .statusCode(400);

    }

    @Test
    void loginUserByPasswordInvalid_shouldReturn400Test() {
        UserLogin userLogin = new UserLogin("user@gmail.com", "");

        given()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_LOGIN)
                .then()
                .statusCode(400);

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
    void removeUserByUuidNull_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        given()
                .header("Authorization", "Bearer "+token)
                .delete(baseURI+":"+port+basePath+BASE_PATH_USER+null)
                .then()
                .statusCode(403)
                .extract()
                .response();

        UserRequestUtil.delete(userResponse.uuid(), token);
    }

    @Test
    void removeUserByUuidDoesNotExist_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        UserRequestUtil.delete(userResponse.uuid(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .delete(baseURI + ":" + port + basePath + BASE_PATH_USER + userResponse.uuid())
                .then()
                .assertThat()
                .statusCode(403);
    }

    @Test
    void removeUserWithInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .delete(baseURI + ":" + port + basePath + BASE_PATH_USER + userResponse.uuid())
                .then()
                .assertThat()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserRequest falseUser = new UserRequest("falseUser", "false@gmail.com", "12345678");

        UserResponse userResponse = UserRequestUtil.post(userRequest);
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);

        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseUserToken = UserRequestUtil.login(falseUserLogin);

        given()
                .header("Authorization", "Bearer " + falseUserToken)
                .delete(baseURI + ":" + port + basePath + BASE_PATH_USER + userResponse.uuid())
                .then()
                .assertThat()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseUserToken);
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

    @Test
    void updateUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserRequest falseUser = new UserRequest("falseUser", "false@gmail.com", "12345678");

        UserResponse userResponse = UserRequestUtil.post(userRequest);
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);

        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseUserToken = UserRequestUtil.login(falseUserLogin);

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        given()
                .header("Authorization", "Bearer "+falseUserToken)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI+":"+port+basePath+BASE_PATH_USER+userResponse.uuid())
                .then()
                .statusCode(403);


        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseUserToken);
    }

    @Test
    void updateUserByUuidNull_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_USER + null)
                .then()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateUserByUuidDoesNotExist_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_USER + "uuid")
                .then()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateUserByNameInvalid_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserLogin userLogin = mockUser.mockUserLogin();

        String token = UserRequestUtil.login(userLogin);

        UserUpdate userUpdate = new UserUpdate("");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_USER + userResponse.uuid())
                .then()
                .statusCode(400);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateUserWithInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);

        UserResponse userResponse = UserRequestUtil.post(userRequest);

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(userUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_USER + userResponse.uuid())
                .then()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }
}