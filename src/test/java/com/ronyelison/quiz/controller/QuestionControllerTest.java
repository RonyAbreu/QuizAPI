package com.ronyelison.quiz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.mock.MockQuestion;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.util.QuestionRequestUtil;
import com.ronyelison.quiz.util.ThemeRequestUtil;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.ronyelison.quiz.util.QuestionRequestUtil.BASE_PATH_QUESTION;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QuestionControllerTest extends QuizApplicationTests {
    MockQuestion mockQuestion = new MockQuestion();
    MockTheme mockTheme = new MockTheme();
    @Test
    void insertQuestionByTitleImageUrlTheme_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);

        QuestionResponse questionResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+themeResponse.id())
                .then()
                .statusCode(201)
                .extract()
                .as(QuestionResponse.class);

        assertNotNull(questionResponse);
        assertNotNull(questionResponse.id());
        assertNotNull(questionResponse.title());
        assertNotNull(questionResponse.theme());
        assertNotNull(questionResponse.imageUrl());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeQuestionByIdAndToken_shouldReturn204Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(204)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findAllQuestions_shouldReturn200Test() {
    }

    @Test
    void findQuestionById_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        QuestionResponse questionReturned = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(200)
                .extract()
                .as(QuestionResponse.class);

        assertNotNull(questionReturned);
        assertNotNull(questionReturned.id());
        assertNotNull(questionReturned.title());
        assertNotNull(questionResponse.imageUrl());
        assertNotNull(questionReturned.theme());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findQuestionsByThemeId_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/theme/"+themeResponse.id())
                .then()
                .statusCode(200)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateQuestionByTitleAndImageUrl_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());
        QuestionUpdate questionUpdate = mockQuestion.mockQuestionUpdate();

        QuestionResponse questionReturned = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(200)
                .extract()
                .as(QuestionResponse.class);

        assertNotNull(questionReturned);
        assertNotNull(questionReturned.id());
        assertNotNull(questionReturned.title());
        assertNotNull(questionResponse.imageUrl());
        assertNotNull(questionReturned.theme());

        assertEquals("Novo titulo", questionReturned.title());
        assertEquals("Nova url", questionReturned.imageUrl());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void find10QuestionsByThemeId_shouldReturn200Test() throws JsonProcessingException {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        for (int i = 1; i < 11; i++) {
            QuestionRequestUtil.post(mockQuestion.mockRequest(i), token, themeResponse.id());
        }

        Response responses = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/quiz/"+themeResponse.id())
                .then()
                .statusCode(200)
                .extract()
                .response();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(responses.body().prettyPrint());

        for (int i = 1; i < 11; i++) {
            QuestionRequestUtil.delete((long) i, token);
        }

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }
}