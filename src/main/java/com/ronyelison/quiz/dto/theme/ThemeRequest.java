package com.ronyelison.quiz.dto.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @NotBlank(message = "Campo tema não pode ser vazio")
        @Size(min = 3, max = 20, message = "Número de caracteres inválido")
        String name) {
}
