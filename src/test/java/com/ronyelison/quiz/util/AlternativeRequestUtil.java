package com.ronyelison.quiz.util;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;

public class AlternativeRequestUtil {
    public static final String BASE_PATH_ALTERNATIVE = "/alternative";
    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }

    public static AlternativeResponse post(AlternativeRequest alternativeRequest, String token, Long questionId){
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE+"/"+questionId)
                .then()
                .statusCode(201)
                .extract()
                .as(AlternativeResponse.class);
    }

    public static void delete(Long id, String token){
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE+"/"+id)
                .then()
                .statusCode(204)
                .assertThat();
    }
}
