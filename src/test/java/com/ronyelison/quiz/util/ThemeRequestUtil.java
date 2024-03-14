package com.ronyelison.quiz.util;

import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;

public class ThemeRequestUtil {

    @BeforeEach
    void setUp(){
        port = 8080;
        baseURI = "http://localhost";
        basePath = "/api/v1";
    }
}
