package com.ronyelison.quiz.dto.question;

import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.entity.Alternative;

import java.util.List;

public record QuestionResponse(
         Long id,
         String title,
         String imageUrl,
         ThemeResponse theme,
         List<AlternativeResponse> alternatives) {
}
