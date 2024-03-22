package br.ufpb.dcx.apps4society.quizapi.dto.question;

import br.ufpb.dcx.apps4society.quizapi.dto.alternative.AlternativeResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;

import java.util.List;

public record QuestionResponse(
         Long id,
         String title,
         String imageUrl,
         ThemeResponse theme,
         List<AlternativeResponse> alternatives) {
}
