package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.question.QuestionMinResponse;
import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
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
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Theme theme;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User creator;
    @Transient
    private final int MAXIMUM_NUMBER_OF_ALTERNATIVES = 4;
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Alternative> alternatives = new ArrayList<>(MAXIMUM_NUMBER_OF_ALTERNATIVES);

    public Question(){

    }

    public Question(QuestionRequest questionRequest, Theme theme, User creator) {
        this.title = questionRequest.title();
        this.imageUrl = questionRequest.imageUrl();
        this.theme = theme;
        this.creator = creator;
    }

    public Question(Long id, String title, String imageUrl, Theme theme, User creator) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.theme = theme;
        this.creator = creator;
    }

    public QuestionResponse entityToResponse(){
        return new QuestionResponse(id,title,imageUrl,theme.entityToResponse(),
                alternatives.stream().map(Alternative::entityToResponse).toList());
    }

    public QuestionMinResponse entityToMinResponse(){
        return new QuestionMinResponse(id, title,imageUrl,theme.entityToResponse());
    }

    public void addAlternative(Alternative alternative) {
        this.alternatives.add(alternative);
    }

    public boolean isFullListOfAlternatives(){
        return this.alternatives.size() == MAXIMUM_NUMBER_OF_ALTERNATIVES;
    }

    public void removeQuestionOfThemeList(Long id){
        theme.getQuestions().removeIf(question -> question.getId().equals(id));
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

    public Theme getTheme() {
        return theme;
    }

    public User getCreator() {
        return creator;
    }
}
