package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.service.ThemeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
