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
		return "<div\n" +
				"      style=\"\n" +
				"        display: flex;\n" +
				"        justify-content: center;\n" +
				"        align-items: center;\n" +
				"        flex-direction: column;\n" +
				"      \"\n" +
				"    >\n" +
				"      <h1\n" +
				"        style=\"\n" +
				"          box-shadow: 0px 0px 5px black;\n" +
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
				"      <a href=\"http://api.observatorioturismopb.com.br:8085/swagger-ui/index.html#/\" target=\"_blank\"\n" +
				"        style=\"\n" +
				"          padding: 15px 20px;\n" +
				"          font-weight: bold;\n" +
				"          font-size: 20px;\n" +
				"          border-radius: 15px;\n" +
				"          cursor: pointer;\n" +
				"          background-color: rgb(0, 134, 36);\n" +
				"          color: #fff;\n" +
				"          text-decoration: none;\n" +
				"        \"\n" +
				"      >\n" +
				"        Documentação\n" +
				"      </a>\n" +
				"    </div>";
	}

}
