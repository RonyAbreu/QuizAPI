package br.ufpb.dcx.apps4society.quizapi.dto.response;

import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionMinResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import br.ufpb.dcx.apps4society.quizapi.dto.alternative.AlternativeResponse;

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
