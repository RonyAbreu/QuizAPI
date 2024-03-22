package br.ufpb.dcx.apps4society.quizapi.util;

import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;

public class QuestionRequestUtil {
    public static final String BASE_PATH_QUESTION = "/question";

    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }

    public static QuestionResponse post(QuestionRequest questionRequest, String token, Long idTheme){
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(questionRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+idTheme)
                .then()
                .statusCode(201)
                .extract()
                .as(QuestionResponse.class);
    }

    public static void delete(Long questionId, String token){
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_QUESTION+"/"+questionId)
                .then()
                .statusCode(204)
                .extract()
                .response();
    }
}
