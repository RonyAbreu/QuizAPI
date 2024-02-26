package com.ronyelison.quiz.dto.question;

import com.ronyelison.quiz.entity.Alternative;

import java.util.List;

public record QuestionResponse(
         Long id,
         String title,
         String imageUrl,
         List<Alternative> alternatives) {
}
