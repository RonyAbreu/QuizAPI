package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.QuizApplicationTests;
import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.util.ThemeRequestUtil;
import com.ronyelison.quiz.util.UserRequestUtil;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static com.ronyelison.quiz.util.ThemeRequestUtil.BASE_PATH_THEME;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class ThemeControllerTest extends QuizApplicationTests {
    MockTheme mockTheme = new MockTheme();
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
}