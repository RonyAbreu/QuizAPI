package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeQuestion(@PathVariable Long id){
        service.removeQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> findAllQuestions(){
        return ResponseEntity.ok(service.findAllQuestions());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<QuestionResponse> findQuestionById(@PathVariable Long id){
        return ResponseEntity.ok(service.findQuestionById(id));
    }

    @GetMapping(value = "/theme")
    public ResponseEntity<List<QuestionResponse>> findThemesByName(@RequestParam(value = "name") String name){
        return ResponseEntity.ok(service.findThemesByName(name));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdate questionUpdate){
        return ResponseEntity.ok(service.updateQuestion(id, questionUpdate));
    }
}
