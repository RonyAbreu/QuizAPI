package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import jakarta.persistence.*;

@Entity(name = "tb_alternative")
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Boolean correct;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    public Alternative(){
    }

    public Alternative(AlternativeRequest alternativeRequest, Question question) {
        this.text = alternativeRequest.text();
        this.correct = alternativeRequest.correct();
        this.question = question;
    }

    public Alternative(Long id, String text, Boolean correct, Question question) {
        this.id = id;
        this.text = text;
        this.correct = correct;
        this.question = question;
    }

    public AlternativeResponse entityToResponse(){
        return new AlternativeResponse(id, text,correct);
    }

    public Long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public Question getQuestion() {
        return question;
    }

    public User getQuestionCreator(){
        return question.getCreator();
    }
}
