package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.dto.alternative.AlternativeUpdate;
import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.mock.MockAlternative;
import com.ronyelison.quiz.mock.MockQuestion;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.util.AlternativeRequestUtil;
import com.ronyelison.quiz.util.QuestionRequestUtil;
import com.ronyelison.quiz.util.ThemeRequestUtil;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.ronyelison.quiz.util.AlternativeRequestUtil.BASE_PATH_ALTERNATIVE;
import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.basePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AlternativeControllerTest extends QuizApplicationTests {
    MockAlternative mockAlternative = new MockAlternative();
    MockQuestion mockQuestion = new MockQuestion();
    MockTheme mockTheme = new MockTheme();
    @Test
    void insertAlternative_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);

        AlternativeResponse alternativeResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE+"/"+questionResponse.id())
                .then()
                .statusCode(201)
                .extract()
                .as(AlternativeResponse.class);


        assertNotNull(alternativeResponse);
        assertNotNull(alternativeResponse.id());
        assertNotNull(alternativeResponse.response());
        assertNotNull(alternativeResponse.correct());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAllAlternatives_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest [] alternativesRequest = mockAlternative.mockAlternativesRequestList();

        List<AlternativeResponse> alternativeResponse = Arrays.stream(given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativesRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
                .then()
                .statusCode(201)
                .extract()
                .as(AlternativeResponse[].class)).toList();

        assertNotNull(alternativeResponse);
        assertEquals(4, alternativeResponse.size());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeByResponse_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockAlternativeRequest(true);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        AlternativeUpdate alternativeUpdate = mockAlternative.mockAlternativeUpdate();

        AlternativeResponse response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE+"/"+alternativeResponse.id())
                .then()
                .statusCode(200)
                .extract()
                .as(AlternativeResponse.class);

        assertNotNull(response);
        assertNotNull(response.id());
        assertNotNull(response.response());
        assertNotNull(response.correct());
        assertEquals("Nova Alternativa", response.response());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }
}