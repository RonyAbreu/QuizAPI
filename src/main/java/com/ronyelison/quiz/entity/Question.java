package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
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
    @OneToMany(mappedBy = "question")
    private List<Alternative> alternatives = new ArrayList<>();

    public Question(){

    }

    public Question(QuestionRequest questionRequest) {
        this.title = questionRequest.title();
        this.imageUrl = questionRequest.imageUrl();
    }

    public QuestionResponse entityToResponse(){
        return new QuestionResponse(id,title,imageUrl,alternatives);
    }

    public void addAlternative(Alternative alternative) throws LimitOfAlternativesException, AlternativeCorrectDuplicateException, FalseAlternativesOnlyException {
        if (this.alternatives.size() == 4){
            throw new LimitOfAlternativesException("Limite máximo de alternativas");
        }
        if (isAlternativeCorrectDuplicate(alternative)) {
            throw new AlternativeCorrectDuplicateException("Já existe uma alternativa verdadeira");
        }
        if (isFalseAlternativesOnly(alternative)){
            throw new FalseAlternativesOnlyException("Não é possível cadastrar apenas alternativas falsas");
        }

        this.alternatives.add(alternative);
    }

    private boolean isAlternativeCorrectDuplicate(Alternative alternative){
        for (Alternative a : this.alternatives){
            if (a.getCorrect() && alternative.getCorrect()){
                return true;
            }
        }
        return false;
    }

    private boolean isFalseAlternativesOnly(Alternative alternative){
        for (Alternative a : this.alternatives){
            if (this.alternatives.size() == 3 && !a.getCorrect() && !alternative.getCorrect()){
                return true;
            }
        }
        return false;
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
