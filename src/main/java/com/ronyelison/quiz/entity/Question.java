package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.service.ThemeService;
import com.ronyelison.quiz.service.exception.AlternativeCorrectDuplicateException;
import com.ronyelison.quiz.service.exception.FalseAlternativesOnlyException;
import com.ronyelison.quiz.service.exception.LimitOfAlternativesException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String imageUrl;
    @ManyToOne
    private Theme theme;
    @Transient
    private final int MAXIMUM_NUMBER_OF_ALTERNATIVES = 4;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Alternative> alternatives = new ArrayList<>(MAXIMUM_NUMBER_OF_ALTERNATIVES);

    public Question(){

    }

    public Question(QuestionRequest questionRequest, Theme theme) {
        this.title = questionRequest.title();
        this.imageUrl = questionRequest.imageUrl();
        this.theme = theme;
    }

    public QuestionResponse entityToResponse(){
        return new QuestionResponse(id,title,imageUrl,theme.entityToResponse(),
                alternatives.stream().map(Alternative::entityToResponse).toList());
    }

    public void addAlternative(Alternative alternative) {
        this.alternatives.add(alternative);
    }

    public boolean isFullListOfAlternatives(){
        return this.alternatives.size() == MAXIMUM_NUMBER_OF_ALTERNATIVES;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Alternative> getAlternatives() {
        return alternatives;
    }
}
