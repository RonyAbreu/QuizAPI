package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;

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
