package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.service.ThemeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theme")
public class ThemeController {
    private ThemeService service;

    @Autowired
    public ThemeController(ThemeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ThemeResponse> insertTheme(@RequestBody @Valid ThemeRequest themeRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insertTheme(themeRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeTheme(@PathVariable Long id){
        service.removeTheme(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> findAllThemes(){
        return ResponseEntity.ok(service.findAllThemes());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ThemeResponse> findThemeById(@PathVariable Long id){
        return ResponseEntity.ok(service.findThemeById(id));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ThemeResponse> updateTheme(@PathVariable Long id,
                                                     @RequestBody @Valid ThemeUpdate themeUpdate){
        return ResponseEntity.ok(service.updateTheme(id,themeUpdate));
    }

}
