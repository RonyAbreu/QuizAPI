package com.ronyelison.quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import jakarta.persistence.*;

@Entity(name = "tb_alternative")
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String response;
    private Boolean correct;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Question question;

    public Alternative(){
    }

    public Alternative(AlternativeRequest alternativeRequest, Question question) {
        this.response = alternativeRequest.response();
        this.correct = alternativeRequest.correct();
        this.question = question;
    }

    public AlternativeResponse entityToResponse(){
        return new AlternativeResponse(id, response,correct);
    }

    public Long getId() {
        return id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
