package com.ronyelison.quiz.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String name,
        String email
) {
}
