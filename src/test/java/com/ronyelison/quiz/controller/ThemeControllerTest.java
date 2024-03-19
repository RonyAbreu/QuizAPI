package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.dto.user.UserLogin;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.mock.MockQuestion;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.util.QuestionRequestUtil;
import com.ronyelison.quiz.util.ThemeRequestUtil;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.ronyelison.quiz.util.ThemeRequestUtil.BASE_PATH_THEME;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class ThemeControllerTest extends QuizApplicationTests {
    MockTheme mockTheme = new MockTheme();
    MockQuestion mockQuestion = new MockQuestion();
    @Test
    void insertThemeByNameAndToken_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_THEME)
                .then()
                .statusCode(201)
                .extract()
                .as(ThemeResponse.class);

        assertNotNull(themeResponse);
        assertNotNull(themeResponse.id());
        assertNotNull(themeResponse.name());

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertThemeByNameInvalid_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = new ThemeRequest("");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_THEME)
                .then()
                .statusCode(400)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertThemeByNameAlreadyExists_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_THEME)
                .then()
                .statusCode(400)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertThemeByInvalidToken_shouldReturn403Test() {
        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_THEME)
                .then()
                .statusCode(403)
                .assertThat();
    }

    @Test
    void removeThemeByIdAndToken_shouldReturn204Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+themeResponse.id())
                .then()
                .statusCode(204);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeThemeByIdIsNull_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+null)
                .then()
                .statusCode(403);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeThemeNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        ThemeRequestUtil.delete(themeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+themeResponse.id())
                .then()
                .statusCode(404);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeThemeWithInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+themeResponse.id())
                .then()
                .statusCode(403);

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeThemeWithLinkedQuestions_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest,token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+themeResponse.id())
                .then()
                .statusCode(400);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeThemeByUserNotHavePermission_shouldReturn403Test() {
        UserResponse userResponse = UserRequestUtil.post(mockUser.mockRequest(1));
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUserRequest = new UserRequest("false", "false@gmail.com", "12345678");
        UserResponse falseUser = UserRequestUtil.post(falseUserRequest);
        UserLogin falseUserLogin = new UserLogin(falseUserRequest.email(), falseUserRequest.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        given()
                .header("Authorization", "Bearer " + falseToken)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+themeResponse.id())
                .then()
                .statusCode(403);

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUser.uuid(), falseToken);
    }

    @Test
    void findAllThemes_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest,token);

        given()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME)
                .then()
                .statusCode(200)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findAllThemesNotFound_shouldReturn404Test() {
        given()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME)
                .then()
                .statusCode(404)
                .assertThat();
    }

    @Test
    void findThemeByIdAndToken_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse postTheme = ThemeRequestUtil.post(themeRequest,token);

        ThemeResponse themeResponse =  given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+ postTheme.id())
                .then()
                .statusCode(200)
                .extract()
                .as(ThemeResponse.class);

        assertNotNull(themeResponse);
        assertNotNull(themeResponse.id());
        assertNotNull(themeResponse.name());

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findThemeByIdIsNull_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + null)
                .then()
                .statusCode(403)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findThemeNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);
        ThemeRequestUtil.delete(themeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + themeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findThemesByName_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest,token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME+"/search?name="+themeRequest.name())
                .then()
                .statusCode(200)
                .assertThat();


        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findThemesByNameIsEmpty_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest,token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME+"/search?name=")
                .then()
                .statusCode(200)
                .assertThat();


        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findThemesByNameNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);

        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest,token);
        ThemeRequestUtil.delete(themeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_THEME+"/search?name="+themeRequest.name())
                .then()
                .statusCode(404)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateThemeByIdAndToken_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse postTheme = ThemeRequestUtil.post(themeRequest,token);
        ThemeUpdate themeUpdate = mockTheme.mockThemeUpdate();

        ThemeResponse themeResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeUpdate)
                .when()
                .patch(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+ postTheme.id())
                .then()
                .statusCode(200)
                .extract()
                .as(ThemeResponse.class);

        assertNotNull(themeResponse);
        assertNotNull(themeResponse.id());
        assertNotNull(themeResponse.name());
        assertEquals("Novo tema", themeResponse.name());

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateThemeByInvalidName_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse postTheme = ThemeRequestUtil.post(themeRequest, token);
        ThemeUpdate themeUpdate = new ThemeUpdate("");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + postTheme.id())
                .then()
                .statusCode(400)
                .assertThat();

        ThemeRequestUtil.delete(postTheme.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateThemeByNameAlreadyExists_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest cineTheme = new ThemeRequest("Cine");
        ThemeRequest gameTheme = new ThemeRequest("Game");

        ThemeResponse postCineTheme = ThemeRequestUtil.post(cineTheme, token);
        ThemeResponse postGameTheme = ThemeRequestUtil.post(gameTheme, token);
        ThemeUpdate themeUpdate = new ThemeUpdate("Game");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(themeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + postCineTheme.id())
                .then()
                .statusCode(400)
                .assertThat();

        ThemeRequestUtil.delete(postCineTheme.id(), token);
        ThemeRequestUtil.delete(postGameTheme.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateThemeByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse postTheme = ThemeRequestUtil.post(themeRequest, token);
        ThemeUpdate themeUpdate = mockTheme.mockThemeUpdate();

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(themeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + postTheme.id())
                .then()
                .statusCode(403)
                .assertThat();

        ThemeRequestUtil.delete(postTheme.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateThemeByUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUserRequest = new UserRequest("false", "false@gmail.com", "12345678");
        UserResponse falseUser = UserRequestUtil.post(falseUserRequest);
        UserLogin falseUserLogin = new UserLogin(falseUserRequest.email(), falseUserRequest.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse postTheme = ThemeRequestUtil.post(themeRequest, token);
        ThemeUpdate themeUpdate = mockTheme.mockThemeUpdate();

        given()
                .header("Authorization", "Bearer " + falseToken)
                .contentType(ContentType.JSON)
                .body(themeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + BASE_PATH_THEME + "/" + postTheme.id())
                .then()
                .statusCode(403)
                .assertThat();

        ThemeRequestUtil.delete(postTheme.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUser.uuid(), falseToken);
    }
}