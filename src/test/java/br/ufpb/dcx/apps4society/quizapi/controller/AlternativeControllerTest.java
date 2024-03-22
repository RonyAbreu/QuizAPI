package br.ufpb.dcx.apps4society.quizapi.controller;

import br.ufpb.dcx.apps4society.quizapi.QuizApplicationTests;
import br.ufpb.dcx.apps4society.quizapi.dto.alternative.AlternativeRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.alternative.AlternativeResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.alternative.AlternativeUpdate;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserLogin;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserResponse;
import br.ufpb.dcx.apps4society.quizapi.mock.MockAlternative;
import br.ufpb.dcx.apps4society.quizapi.mock.MockQuestion;
import br.ufpb.dcx.apps4society.quizapi.mock.MockTheme;
import br.ufpb.dcx.apps4society.quizapi.util.AlternativeRequestUtil;
import br.ufpb.dcx.apps4society.quizapi.util.QuestionRequestUtil;
import br.ufpb.dcx.apps4society.quizapi.util.ThemeRequestUtil;
import br.ufpb.dcx.apps4society.quizapi.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.basePath;
import static org.junit.jupiter.api.Assertions.*;


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
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+questionResponse.id())
                .then()
                .statusCode(201)
                .extract()
                .as(AlternativeResponse.class);


        assertNotNull(alternativeResponse);
        assertNotNull(alternativeResponse.id());
        assertNotNull(alternativeResponse.text());
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
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
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
    void insertAlternativeByInvalidText_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = new AlternativeRequest("", true);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + questionResponse.id())
                .then()
                .statusCode(400)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAlternativeByInvalidCorrect_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = new AlternativeRequest("alternative", null);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + questionResponse.id())
                .then()
                .statusCode(400)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAlternativeByQuestionNotFound_shouldReturn404Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockAlternativeRequest(false);

        QuestionRequestUtil.delete(questionResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + questionResponse.id())
                .then()
                .statusCode(404)
                .assertThat();


        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAllAlternativesFalses_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest [] alternativesFalsesRequest = mockAlternative.mockAlternativesFalsesRequestList();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativesFalsesRequest)
                .when()
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
                .then()
                .statusCode(400)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAlternativesTrueDuplicate_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest [] alternativesTrueDuplicateRequest = mockAlternative.mockAlternativesTrueDuplicateRequestList();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativesTrueDuplicateRequest)
                .when()
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
                .then()
                .statusCode(400)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAlternativesMoreThanTheLimit_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest [] alternativesMoreThanTheLimit = mockAlternative.mockAlternativesRequestListMoreThanTheLimit();

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativesMoreThanTheLimit)
                .when()
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
                .then()
                .statusCode(400)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void insertAlternativesByInvalidToken_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest [] alternativeRequests = mockAlternative.mockAlternativesRequestList();

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(alternativeRequests)
                .when()
                .post(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/all/"+questionResponse.id())
                .then()
                .statusCode(403)
                .assertThat();

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
                .patch(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+alternativeResponse.id())
                .then()
                .statusCode(200)
                .extract()
                .as(AlternativeResponse.class);

        assertNotNull(response);
        assertNotNull(response.id());
        assertNotNull(response.text());
        assertNotNull(response.correct());
        assertEquals("Nova Alternativa", response.text());

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeByNullId_shouldReturn403Test() {
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

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + null)
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeNotFound_shouldReturn404Test() {
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

        AlternativeRequestUtil.delete(alternativeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + alternativeResponse.id())
                .then()
                .assertThat()
                .statusCode(404);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeByInvalidToken_shouldReturn403Test() {
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

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + alternativeResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeByInvalidText_shouldReturn400Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockAlternativeRequest(true);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        AlternativeUpdate alternativeUpdate = new AlternativeUpdate("");

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + alternativeResponse.id())
                .then()
                .assertThat()
                .statusCode(400);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void updateAlternativeByUserNotHavePermission_shouldReturn403Test() {
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUser = new UserRequest("false", "false@gmail.com","false12345");
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);
        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockAlternativeRequest(true);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        AlternativeUpdate alternativeUpdate = mockAlternative.mockAlternativeUpdate();

        given()
                .header("Authorization", "Bearer " + falseToken)
                .contentType(ContentType.JSON)
                .body(alternativeUpdate)
                .when()
                .patch(baseURI + ":" + port + basePath + AlternativeRequestUtil.BASE_PATH_ALTERNATIVE + "/" + alternativeResponse.id())
                .then()
                .assertThat()
                .statusCode(403);

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseToken);
    }

    @Test
    void removeAlternativeById_shouldReturn204Test(){
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+alternativeResponse.id())
                .then()
                .statusCode(204)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeAlternativeByInvalidId_shouldReturn403Test(){
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+ null)
                .then()
                .statusCode(403)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeAlternativeNotFound_shouldReturn404Test(){
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());
        AlternativeRequestUtil.delete(alternativeResponse.id(), token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+ alternativeResponse.id())
                .then()
                .statusCode(404)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeAlternativeByInvalidToken_shouldReturn403Test(){
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        given()
                .header("Authorization", "Bearer " + INVALID_TOKEN)
                .when()
                .delete(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+ alternativeResponse.id())
                .then()
                .statusCode(403)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
    }

    @Test
    void removeAlternativeByUserNotHavePermission_shouldReturn403Test(){
        UserRequest userRequest = mockUser.mockRequest(1);
        UserResponse userResponse = UserRequestUtil.post(userRequest);
        String token = UserRequestUtil.login(mockUser.mockUserLogin());

        UserRequest falseUser = new UserRequest("false", "false@gmail.com","false12345");
        UserResponse falseUserResponse = UserRequestUtil.post(falseUser);
        UserLogin falseUserLogin = new UserLogin(falseUser.email(), falseUser.password());
        String falseToken = UserRequestUtil.login(falseUserLogin);

        ThemeRequest themeRequest = mockTheme.mockRequest(1);
        ThemeResponse themeResponse = ThemeRequestUtil.post(themeRequest, token);

        QuestionRequest questionRequest = mockQuestion.mockRequest(1);
        QuestionResponse questionResponse = QuestionRequestUtil.post(questionRequest, token, themeResponse.id());

        AlternativeRequest alternativeRequest = mockAlternative.mockRequest(1);
        AlternativeResponse alternativeResponse = AlternativeRequestUtil.post(alternativeRequest, token, questionResponse.id());

        given()
                .header("Authorization", "Bearer " + falseToken)
                .when()
                .delete(baseURI+":"+port+basePath+ AlternativeRequestUtil.BASE_PATH_ALTERNATIVE+"/"+ alternativeResponse.id())
                .then()
                .statusCode(403)
                .assertThat();

        QuestionRequestUtil.delete(questionResponse.id(), token);
        ThemeRequestUtil.delete(themeResponse.id(), token);
        UserRequestUtil.delete(userResponse);
        UserRequestUtil.delete(falseUserResponse.uuid(), falseToken);
    }
}