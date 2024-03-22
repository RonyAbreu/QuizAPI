package br.ufpb.dcx.apps4society.quizapi.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdate(
        @NotBlank(message = "Campo name não pode ser vazio")
        @Size(min = 3, max = 100, message = "Número de caracteres inválido")
        String name
) {
}
