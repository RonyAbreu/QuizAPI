package br.ufpb.dcx.apps4society.quizapi.mock;

import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionUpdate;
import br.ufpb.dcx.apps4society.quizapi.entity.Question;
import br.ufpb.dcx.apps4society.quizapi.entity.Theme;
import br.ufpb.dcx.apps4society.quizapi.entity.User;

import java.util.ArrayList;
import java.util.List;

public class MockQuestion implements MockInterface<Question, QuestionRequest>{
    public Question mockEntity(Integer num, Theme theme, User user) {
        return new Question(num.longValue(),
                "Question",
                "imagem.com",
                theme,
                user
        );
    }

    @Override
    public Question mockEntity(Integer num) {
        return new Question(num.longValue(),
                "Question",
                "imagem.com",
                new Theme(),
                new User()
        );
    }

    @Override
    public QuestionRequest mockRequest(Integer num) {
        return new QuestionRequest("Question",
                "imagem.com");
    }

    public List<Question> mockList(Integer size){
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            questions.add(mockEntity(i));
        }
        return questions;
    }

    public QuestionUpdate mockQuestionUpdate(){
        return new QuestionUpdate("Novo titulo", "Nova url");
    }
}
