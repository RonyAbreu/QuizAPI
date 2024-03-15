package com.ronyelison.quiz.util;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.entity.Alternative;
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

    public static AlternativeResponse post(AlternativeRequest alternativeRequest, String token){
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(alternativeRequest)
                .when()
                .post(baseURI+":"+port+basePath+BASE_PATH_ALTERNATIVE)
                .then()
                .statusCode(201)
                .extract()
                .as(AlternativeResponse.class);
    }

    public static AlternativeResponse [] postAll(AlternativeRequest [] alternativeRequests, String token){
        AlternativeResponse [] listOfAlternativeResponse = new AlternativeResponse[alternativeRequests.length];

        for (int i = 0; i < alternativeRequests.length; i++) {
            listOfAlternativeResponse[i] = post(alternativeRequests[i], token);
        }

        return listOfAlternativeResponse;
    }
}
