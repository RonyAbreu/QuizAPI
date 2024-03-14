package com.ronyelison.quiz.util;

import com.ronyelison.quiz.dto.response.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

import static io.restassured.RestAssured.*;

public class ResponseRequestUtil {
    public static final String BASE_PATH_RESPONSE = "/response";

    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }

    public static ResponseDTO post(UUID userId, Long questionId, Long alternativeId, String token){
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_RESPONSE+"/"+userId+"/"+questionId+"/"+alternativeId)
                .then()
                .statusCode(201)
                .extract()
                .as(ResponseDTO.class);
    }

    public static void delete(Long idResponse, String token){
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(baseURI+":"+port+basePath+BASE_PATH_RESPONSE+"/"+idResponse)
                .then()
                .statusCode(204)
                .extract()
                .response();
    }
}
