package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.repository.ThemeRepository;
import com.ronyelison.quiz.service.exception.ThemeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private ThemeRepository repository;

    @Autowired
    public ThemeService(ThemeRepository repository) {
        this.repository = repository;
    }

    public ThemeResponse insertTheme(ThemeRequest themeRequest){
        Theme theme = new Theme(themeRequest.name());
        repository.save(theme);
        return theme.entityToResponse();
    }

    public void removeTheme(Long id){
        Theme theme = repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("Tema não encontrado"));
        repository.delete(theme);
    }

    public List<ThemeResponse> findAllThemes(){
        return repository.findAll()
                .stream()
                .map(Theme::entityToResponse)
                .toList();
    }

    public ThemeResponse findThemeById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("Tema não encontrado"))
                .entityToResponse();
    }

    public ThemeResponse updateTheme(Long id, ThemeUpdate themeUpdate){
        Theme theme = repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException("Tema não encontrado"));

        updateData(theme, themeUpdate);
        repository.save(theme);

        return theme.entityToResponse();
    }

    private void updateData(Theme theme, ThemeUpdate themeUpdate){
        theme.setName(themeUpdate.name());
    }
}
