package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.service.AlternativeService;
import com.ronyelison.quiz.service.exception.AlternativeCorrectDuplicateException;
import com.ronyelison.quiz.service.exception.FalseAlternativesOnlyException;
import com.ronyelison.quiz.service.exception.LimitOfAlternativesException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alternative")
public class AlternativeController {
    private AlternativeService service;

    @Autowired
    public AlternativeController(AlternativeService service) {
        this.service = service;
    }

    @PostMapping(value = "/{idQuestion}")
    public ResponseEntity<AlternativeResponse> insertAlternative(@RequestBody @Valid AlternativeRequest alternative, @PathVariable Long idQuestion)
            throws FalseAlternativesOnlyException, AlternativeCorrectDuplicateException, LimitOfAlternativesException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertAlternative(alternative, idQuestion));
    }
}
