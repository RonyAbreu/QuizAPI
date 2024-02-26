package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {
    private QuestionService service;

    @Autowired
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @PostMapping(value = "/{idTheme}")
    public ResponseEntity<QuestionResponse> insertQuestion(@RequestBody @Valid QuestionRequest questionRequest, @PathVariable Long idTheme){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertQuestion(questionRequest, idTheme));
    }
}
