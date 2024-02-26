package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.entity.Alternative;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.repository.AlternativeRepository;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.service.exception.AlternativeCorrectDuplicateException;
import com.ronyelison.quiz.service.exception.FalseAlternativesOnlyException;
import com.ronyelison.quiz.service.exception.LimitOfAlternativesException;
import com.ronyelison.quiz.service.exception.QuestionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlternativeService {
    private AlternativeRepository alternativeRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public AlternativeService(AlternativeRepository alternativeRepository, QuestionRepository questionRepository) {
        this.alternativeRepository = alternativeRepository;
        this.questionRepository = questionRepository;
    }

    public AlternativeResponse insertAlternative(AlternativeRequest alternativeRequest, Long idQuestion) throws FalseAlternativesOnlyException, AlternativeCorrectDuplicateException, LimitOfAlternativesException {
        Question question = questionRepository.findById(idQuestion)
                .orElseThrow(() -> new QuestionNotFoundException("A questão não foi encontrada"));

        Alternative alternative = new Alternative(alternativeRequest, question);
        question.addAlternative(alternative);

        alternativeRepository.save(alternative);
        return alternative.entityToResponse();
    }
}
