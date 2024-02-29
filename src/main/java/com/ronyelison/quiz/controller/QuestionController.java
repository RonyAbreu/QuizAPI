package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.service.QuestionService;
import com.ronyelison.quiz.service.exception.InvalidUserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@Tag(name = "Question", description = "Questions of Quiz")
public class QuestionController {
    private QuestionService service;

    @Autowired
    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @Operation(tags = "Question", summary = "Insert Question", responses ={
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Theme Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @PostMapping(value = "/{idTheme}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> insertQuestion(@RequestBody @Valid QuestionRequest questionRequest, @PathVariable Long idTheme,
                                                           @RequestHeader(value = "Authorization") String token) throws InvalidUserException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertQuestion(questionRequest, idTheme, token));
    }

    @Operation(tags = "Question", summary = "Remove Question", responses ={
            @ApiResponse(description = "No content", responseCode = "204", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeQuestion(@PathVariable Long id){
        service.removeQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(tags = "Question", summary = "Find All Questions", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionResponse.class)))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content())
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionResponse>> findAllQuestions(){
        return ResponseEntity.ok(service.findAllQuestions());
    }

    @Operation(tags = "Question", summary = "Find Question", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> findQuestionById(@PathVariable Long id){
        return ResponseEntity.ok(service.findQuestionById(id));
    }

    @Operation(tags = "Question", summary = "Find Questions by Theme Name", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = QuestionResponse.class)))),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
    } )
    @GetMapping(value = "/theme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionResponse>> findQuestionByThemesName(@RequestParam(value = "name") String name){
        return ResponseEntity.ok(service.findQuestionByThemesName(name));
    }

    @Operation(tags = "Question", summary = "Update Question", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @PutMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Long id, @RequestBody QuestionUpdate questionUpdate){
        return ResponseEntity.ok(service.updateQuestion(id, questionUpdate));
    }
}
