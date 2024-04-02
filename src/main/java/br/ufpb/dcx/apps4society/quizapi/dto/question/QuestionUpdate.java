package br.ufpb.dcx.apps4society.quizapi.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record QuestionUpdate(
        @NotBlank(message = "Campo de titulo não pode ser vazio")
        @Size(min = 4 ,max = 500, message = "Número de caracteres inválido")
        String title,
        @Size(max = 255, message = "Número de caracteres inválido")
        @URL(message = "URL inválida")
        String imageUrl) {
}
