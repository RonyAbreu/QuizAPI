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
    private String name;
    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    public Theme(){

    }

    public boolean containsQuestionsInTheList(){
        return !questions.isEmpty();
    }

    public Theme(String name) {
        this.name = name;
    }

    public ThemeResponse entityToResponse(){
        return new ThemeResponse(id, name);
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
