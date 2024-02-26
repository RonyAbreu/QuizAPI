package com.ronyelison.quiz.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionRequest(
        @NotBlank(message = "Campo de titulo não pode ser vazio")
        @Size(min = 4 ,max = 500, message = "Número de caracteres inválido")
        String title,
        String imageUrl) {
}
