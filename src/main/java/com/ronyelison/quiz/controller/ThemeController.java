package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.service.ThemeService;
import com.ronyelison.quiz.service.exception.ThemeAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theme")
@Tag(name = "Theme", description = "Themes of Questions")
public class ThemeController {
    private ThemeService service;

    @Autowired
    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @Operation(tags = "Theme", summary = "Insert Theme", responses ={
            @ApiResponse(description = "Success", responseCode = "201", content = @Content(schema = @Schema(implementation = ThemeResponse.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeResponse> insertTheme(@RequestBody @Valid ThemeRequest themeRequest, @RequestHeader("Authorization") String token) throws ThemeAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertTheme(themeRequest, token));
    }

    @Operation(tags = "Theme", summary = "Remove Theme", responses ={
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content()),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id, @RequestHeader("Authorization") String token) throws UserNotHavePermissionException {
        service.removeTheme(id, token);
        return ResponseEntity.noContent().build();
    }

    @Operation(tags = "Theme", summary = "Find All Themes", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ThemeResponse.class)))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ThemeResponse>> findAllThemes(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "30") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findAllThemes(pageable));
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Page<ThemeResponse>> findThemesByName(@RequestParam(value = "name", defaultValue = "") String name,
                                                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "30") Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(service.findThemesByName(name, pageable));
    }

    @Operation(tags = "Theme", summary = "Find Theme", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ThemeResponse.class))),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
    })
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeResponse> findThemeById(@PathVariable Long id){
        return ResponseEntity.ok(service.findThemeById(id));
    }

    @Operation(tags = "Theme", summary = "Update Theme", responses ={
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = ThemeResponse.class))),
            @ApiResponse(description = "not Found", responseCode = "404", content = @Content()),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content()),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content()),
            @ApiResponse(description = "Unauthorized", responseCode = "403", content = @Content())
    } )
    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeResponse> updateTheme(@PathVariable Long id,
                                                     @RequestBody @Valid ThemeUpdate themeUpdate,
                                                     @RequestHeader("Authorization") String token) throws UserNotHavePermissionException {
        return ResponseEntity.ok(service.updateTheme(id,themeUpdate, token));
    }

}
