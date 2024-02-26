package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.theme.ThemeResponse;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String theme;
    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    public Theme(){

    }

    public Theme(String theme) {
        this.theme = theme;
    }

    public ThemeResponse entityToResponse(){
        return new ThemeResponse(id, theme);
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
