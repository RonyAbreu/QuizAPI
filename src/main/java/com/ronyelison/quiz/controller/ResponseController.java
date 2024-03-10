package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.response.ResponseDTO;
import com.ronyelison.quiz.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/response")
public class ResponseController {
    private ResponseService service;

    @Autowired
    public ResponseController(ResponseService service) {
        this.service = service;
    }

    @PostMapping(value = "/{idUser}/{idQuestion}/{idAlternative}")
    public ResponseEntity<ResponseDTO> insertResponse(@PathVariable UUID idUser,
                                                      @PathVariable Long idQuestion,
                                                      @PathVariable Long idAlternative){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertResponse(idUser, idQuestion, idAlternative));
    }
}
