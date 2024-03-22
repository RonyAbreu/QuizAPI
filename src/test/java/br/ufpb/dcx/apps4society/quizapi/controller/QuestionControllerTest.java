package br.ufpb.dcx.apps4society.quizapi.controller;

import br.ufpb.dcx.apps4society.quizapi.QuizApplicationTests;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionUpdate;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserLogin;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserResponse;
import br.ufpb.dcx.apps4society.quizapi.mock.MockQuestion;
import br.ufpb.dcx.apps4society.quizapi.mock.MockTheme;
import br.ufpb.dcx.apps4society.quizapi.util.QuestionRequestUtil;
import br.ufpb.dcx.apps4society.quizapi.util.ThemeRequestUtil;
import br.ufpb.dcx.apps4society.quizapi.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static br.ufpb.dcx.apps4society.quizapi.util.QuestionRequestUtil.BASE_PATH_QUESTION;
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
                .post(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+themeResponse.id())
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
    void insertQuestionByTitleAndImageUrlIsEmpty_shouldReturn201Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionRequest questionRequest = new QuestionRequest("title", "");

        QuestionResponse questionResponse = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+themeResponse.id())
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
    void insertQuestionWithInvalidTitle_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionRequest questionRequest = new QuestionRequest("", "image.com");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + themeResponse.id())
                .then()
                .assertThat()
                .statusCode(400);

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertQuestionByThemeNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);

        ThemeRequestUtil.delete(themeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + themeResponse.id())
                .then()
                .assertThat()
                .statusCode(404);

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertQuestionByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + themeResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

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
                .delete(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(204)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeQuestionNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        QuestionRequestUtil.delete(questionResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeQuestionByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(403)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeQuestionByUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUser = new UserRequest("false", "false@gmail.com","false12345");
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);
        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + falseToken)
                .when()
                .delete(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .statusCode(403)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseToken);
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
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
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
    void findQuestionNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        QuestionRequestUtil.delete(questionResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .assertThat()
                .statusCode(404);

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findQuestionByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findQuestionByUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUser = new UserRequest("false", "false@gmail.com","false12345");
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);
        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + falseToken)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseToken);
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
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/theme/"+themeResponse.id())
                .then()
                .statusCode(200)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findQuestionsByThemeNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/theme/"+themeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void findQuestionsByThemeIdButEmptyList_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/theme/"+themeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

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
                .put(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
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
    void updateQuestionByTitleIsValidAndImageIsEmpty_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());
        QuestionUpdate questionUpdate = new QuestionUpdate("Novo titulo", "");

        QuestionResponse questionReturned = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/"+questionResponse.id())
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
        assertEquals("", questionReturned.imageUrl());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateQuestionByUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUser = new UserRequest("false", "false@gmail.com","false12345");
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);
        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());
        QuestionUpdate questionUpdate = new QuestionUpdate("Novo titulo", "");

        given()
                .header("Authorization", "Bearer " + falseToken)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + questionResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseToken);
    }

    @Test
    void updateQuestionNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        QuestionRequestUtil.delete(questionResponse.id(), token);

        QuestionUpdate questionUpdate = mockQuestion.mockQuestionUpdate();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + questionResponse.id())
                .then()
                .assertThat()
                .statusCode(404);

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateQuestionByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());
        QuestionUpdate questionUpdate = mockQuestion.mockQuestionUpdate();

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + questionResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateQuestionByInvalidTitle_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());
        QuestionUpdate questionUpdate = new QuestionUpdate("", "image.com");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionUpdate)
                .when()
                .put(baseURI + ":" + port + basePath + BASE_PATH_QUESTION + "/" + questionResponse.id())
                .then()
                .assertThat()
                .statusCode(400);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void find10QuestionsByThemeId_shouldReturn200Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        QuestionResponse questionResponse = QuestionRequestUtil.post(mockQuestion.mockRequest(1), token, themeResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/quiz/"+themeResponse.id())
                .then()
                .statusCode(200)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void find10QuestionsByThemeNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);


        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/quiz/"+themeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        UserRequestUtil.delete(userResponse);
    }

    @Test
    void find10QuestionsWithThemeByListIsEmpty_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeResponse themeResponse = ThemeRequestUtil.post(mockTheme.mockRequest(1), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(baseURI+":"+port+basePath+ BASE_PATH_QUESTION+"/quiz/"+themeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }
}