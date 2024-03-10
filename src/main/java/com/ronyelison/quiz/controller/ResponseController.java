package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.response.ResponseDTO;
import com.ronyelison.quiz.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<ResponseDTO>> findAllResponses(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "20") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findAllResponses(pageable));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<Page<ResponseDTO>> findResponsesByUser(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                              @PathVariable UUID userId){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findResponsesByUser(pageable, userId));
    }
}
