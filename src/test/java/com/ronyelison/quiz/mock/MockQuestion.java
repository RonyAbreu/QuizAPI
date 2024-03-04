package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;

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
    public QuestionRequest mockDTO(Integer num) {
        return new QuestionRequest("Question",
                "imagem.com");
    }
}
