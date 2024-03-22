package br.ufpb.dcx.apps4society.quizapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class QuizApplication {
	@Value("${app.version}")
	private String version;

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args);
	}

	@GetMapping("/")
	@ResponseBody
	public String welcome(){
		return "Bem vindo a QUIZ-API ".concat(version);
	}

}
