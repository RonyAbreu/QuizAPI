package com.ronyelison.quiz.util;

import org.junit.jupiter.api.BeforeEach;
import static io.restassured.RestAssured.port;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.basePath;

public class AlternativeRequestUtil {

    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }
}
