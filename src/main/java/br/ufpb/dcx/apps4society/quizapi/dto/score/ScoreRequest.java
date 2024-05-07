package br.ufpb.dcx.apps4society.quizapi.dto.score;

import jakarta.validation.constraints.NotNull;

public record ScoreRequest(
        @NotNull(message = "Esse campo não pode estar vazio")
        Integer numberOfHits,
        @NotNull(message = "Esse campo não pode estar vazio")
        Integer totalTime) {
}
