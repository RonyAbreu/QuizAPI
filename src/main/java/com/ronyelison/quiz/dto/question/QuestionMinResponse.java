package com.ronyelison.quiz.dto.question;

import com.ronyelison.quiz.dto.theme.ThemeResponse;

public record QuestionMinResponse(
        Long id,
        String title,
        String imageUrl,
        ThemeResponse theme
) {
}
