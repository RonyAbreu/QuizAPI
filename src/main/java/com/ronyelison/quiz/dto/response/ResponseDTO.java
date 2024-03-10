package com.ronyelison.quiz.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.dto.question.QuestionMinResponse;
import com.ronyelison.quiz.dto.user.UserResponse;

import java.time.LocalDateTime;

public record ResponseDTO(
        Long id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dateTime,
        UserResponse user,
        QuestionMinResponse question,
        AlternativeResponse alternative
) {
}
