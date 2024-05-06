package br.ufpb.dcx.apps4society.quizapi.controller;

import br.ufpb.dcx.apps4society.quizapi.dto.score.ScoreRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.score.ScoreResponse;
import br.ufpb.dcx.apps4society.quizapi.service.ScoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/score")
public class ScoreController {
    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping(value = "/{userId}/{themeId}")
    public ResponseEntity<ScoreResponse> insertScore(@RequestBody ScoreRequest scoreRequest,
                                                     @PathVariable UUID userId,
                                                     @PathVariable Long themeId){
        return ResponseEntity.status(HttpStatus.CREATED).body(scoreService.insertScore(scoreRequest, userId, themeId));
    }

    @GetMapping(value = "/{themeId}")
    public ResponseEntity<List<ScoreResponse>> findRankingByTheme(@PathVariable Long themeId){
        return ResponseEntity.ok(scoreService.findRankingByTheme(themeId));
    }
}
