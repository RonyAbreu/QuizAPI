package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.entity.*;

import java.util.ArrayList;
import java.util.List;

public class MockResponse {
    public Response mockResponseDTO(Integer num) {
        return new Response(
                new User("User", "user@gmail.com", "12345678"),
                new Question(new QuestionRequest("title", "image.com"), new Theme(), new User()),
                new Alternative(new AlternativeRequest("response", true), new Question()));
    }

    public List<Response> mockList(Integer size) {
        List<Response> responseDTOList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            responseDTOList.add(mockResponseDTO(i));
        }
        return responseDTOList;
    }

}
