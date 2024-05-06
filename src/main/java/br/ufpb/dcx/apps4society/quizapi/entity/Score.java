package br.ufpb.dcx.apps4society.quizapi.entity;

import br.ufpb.dcx.apps4society.quizapi.dto.score.ScoreResponse;
import jakarta.persistence.*;

import java.io.Serializable;
import java.text.DecimalFormat;

@Entity(name = "tb_score")
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "result")
    private Double finalResult;
    @Transient
    private Integer numberOfHits;
    @Transient
    private Integer totalTime;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Theme theme;

    public Score(){}

    public Score(Integer numberOfHits, Integer totalTime, User user, Theme theme) {
        this.finalResult = calculateResult(numberOfHits, totalTime);
        this.user = user;
        this.theme = theme;
    }

    private Double calculateResult(Integer numberOfHits, Integer totalTime){
        double hitValue = 80.4;
        double reduceValue = 1.5;
        double result = (numberOfHits * hitValue) - (totalTime * reduceValue);
        String df = new DecimalFormat("#.##").format(result).replace(",",".");
        return Double.valueOf(df);
    }

    public ScoreResponse entityToResponse() {
        return new ScoreResponse(id,finalResult,user.entityToResponse(),theme.entityToResponse());
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Theme getTheme() {
        return theme;
    }

    public Double getFinalResult() {
        return finalResult;
    }

    public Integer getNumberOfHits() {
        return numberOfHits;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

}
