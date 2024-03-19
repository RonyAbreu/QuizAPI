package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeUpdate;
import com.ronyelison.quiz.entity.Alternative;
import com.ronyelison.quiz.entity.Question;

public class MockAlternative implements MockInterface<Alternative, AlternativeRequest>{

    public Alternative mockEntity(Integer num, Boolean correct, Question question) {
        return new Alternative(num.longValue(),
                "Alternative",
                correct,
                question);
    }
    @Override
    public Alternative mockEntity(Integer num) {
        return new Alternative(num.longValue(),
                "Alternative",
                false,
                new Question());
    }

    @Override
    public AlternativeRequest mockRequest(Integer num) {
        return new AlternativeRequest("Alternative", false);
    }

    public AlternativeRequest mockAlternativeRequest(Boolean correct){
        return new AlternativeRequest("Alternative", correct);
    }

    public AlternativeUpdate mockAlternativeUpdate(){
        return new AlternativeUpdate("Nova Alternativa");
    }

    public AlternativeRequest[] mockAlternativesRequestList() {
        return new AlternativeRequest[]{
                new AlternativeRequest("alt 1", false),
                new AlternativeRequest("alt 2", false),
                new AlternativeRequest("alt 3", false),
                new AlternativeRequest("alt 4", true),
        };
    }
}
