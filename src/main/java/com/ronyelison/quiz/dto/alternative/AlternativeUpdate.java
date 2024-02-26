package com.ronyelison.quiz.dto.alternative;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlternativeUpdate(
        @NotBlank(message = "Campo de resposta não pode ser vazio")
        @Size(min = 1, max = 255, message = "Número de caracteres inválido")
        String response) {
}
