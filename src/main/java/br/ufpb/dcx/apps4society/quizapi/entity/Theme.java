package br.ufpb.dcx.apps4society.quizapi.entity;

import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "tb_theme")
public class Theme implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User creator;
    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();

    public Theme(){

    }

    public Theme(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public Theme(Long id, String name, User creator) {
        this.id = id;
        this.name = name;
        this.creator = creator;
    }

    public ThemeResponse entityToResponse(){
        return new ThemeResponse(id, name);
    }

    public void addQuestion(Question question){
        this.questions.add(question);
    }
    public boolean containsQuestionsInTheList(){
        return !questions.isEmpty();
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

    public User getCreator() {
        return creator;
    }
}
