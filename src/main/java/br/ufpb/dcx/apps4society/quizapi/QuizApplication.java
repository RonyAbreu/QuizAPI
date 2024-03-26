package br.ufpb.dcx.apps4society.quizapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class QuizApplication {
	@Value("${app.version}")
	private String version;

	public static void main(String[] args) {
		SpringApplication.run(QuizApplication.class, args);
	}

	@GetMapping("/")
	public String welcome(){
		return welcomeHtml();
	}

	private String welcomeHtml(){
		return "<div style=\"display: flex; justify-content: center;\">\n" +
				"      <h1\n" +
				"        style=\"\n" +
				"          box-shadow: 0px 0px 8px black;\n" +
				"          border-radius: 15px;\n" +
				"          width: 500px;\n" +
				"          height: 80px;\n" +
				"          display: flex;\n" +
				"          align-items: center;\n" +
				"          justify-content: center;\n" +
				"        \"\n" +
				"      >\n" +
				"        Bem-vindo ao QuizAPI <span style=\"color: green; margin-left: 10px;\">"+version+"</span>\n" +
				"      </h1>\n" +
				"    </div>";
	}

}
