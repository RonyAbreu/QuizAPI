package br.ufpb.dcx.apps4society.quizapi.dto.score;

import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserResponse;

public record ScoreResponse(
        Long id,
        Double result,
        UserResponse user,
        ThemeResponse theme
) {
}
