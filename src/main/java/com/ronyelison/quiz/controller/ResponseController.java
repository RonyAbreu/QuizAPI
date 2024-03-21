package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.response.ResponseDTO;
import com.ronyelison.quiz.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/response")
@Tag(name = "Response", description = "Responses of Users")
public class ResponseController {
    private ResponseService service;

    @Autowired
    public ResponseController(ResponseService service) {
        this.service = service;
    }

    @Operation(tags = "Response", summary = "Insert Response", responses ={
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @PostMapping(value = "/{idUser}/{idQuestion}/{idAlternative}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> insertResponse(@PathVariable UUID idUser,
                                                      @PathVariable Long idQuestion,
                                                      @PathVariable Long idAlternative){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertResponse(idUser, idQuestion, idAlternative));
    }

    @Operation(tags = "Response", summary = "Find All Responses", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDTO.class)))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ResponseDTO>> findAllResponses(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "20") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findAllResponses(pageable));
    }

    @Operation(tags = "Response", summary = "Find Responses by User", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDTO.class)))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ResponseDTO>> findResponsesByUser(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                              @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                              @RequestHeader("Authorization") String token){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findResponsesByUser(pageable, token));
    }

    @Operation(tags = "Response", summary = "Find Responses by Question Creator", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ResponseDTO.class)))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @GetMapping(value = "/questions/creator")
    public ResponseEntity<Page<ResponseDTO>> findResponsesByQuestionCreator(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                            @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                                            @RequestHeader("Authorization") String token){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findResponsesByQuestionCreator(pageable,token));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeResponse(@PathVariable Long id){
        service.removeResponse(id);
        return ResponseEntity.noContent().build();
    }
}
