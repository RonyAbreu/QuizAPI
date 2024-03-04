package com.ronyelison.quiz;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class QuizApplicationTests {

	@BeforeAll
	public static void setUp(){
		RestAssured.port = 8080;
		RestAssured.baseURI = "http://localhost:8080";
		RestAssured.basePath = "/api/v1";
	}

}
