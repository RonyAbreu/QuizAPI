package br.ufpb.dcx.apps4society.quizapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Campo name não pode ser vazio")
        @Size(min = 3, max = 30, message = "Número de caracteres inválido")
        String name,
        @NotBlank(message = "Campo email não pode ser vazio")
        @Size(max = 100, message = "Número de caracteres inválido")
        @Email(message = "Email inválido")
        String email,
        @NotBlank(message = "Campo password não pode ser vazio")
        @Size(min = 8, max = 20, message = "Seu password precisa ter entre 8-20 caracteres")
        String password
) {
}
