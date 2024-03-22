package br.ufpb.dcx.apps4society.quizapi.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String name,
        String email
) {
}
