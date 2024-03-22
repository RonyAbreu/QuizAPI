package br.ufpb.dcx.apps4society.quizapi.util;

import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;

public class ThemeRequestUtil {
    public static final String BASE_PATH_THEME = "/theme";

    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }


    public static ThemeResponse post(ThemeRequest themeRequest, String token){
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(themeRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_THEME)
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(ThemeResponse.class);
    }

    public static void delete(Long idTheme, String token){
        given()
                .header("Authorization", "Bearer " + token)
                .delete(baseURI+":"+port+basePath+BASE_PATH_THEME+"/"+idTheme)
                .then()
                .statusCode(204)
                .extract()
                .response();
    }
}
