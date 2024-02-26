package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.dto.alternative.AlternativeUpdate;
import com.ronyelison.quiz.entity.Alternative;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.repository.AlternativeRepository;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.service.exception.*;
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

    public AlternativeResponse insertAlternative(AlternativeRequest alternativeRequest, Long idQuestion)
            throws FalseAlternativesOnlyException, AlternativeCorrectDuplicateException, LimitOfAlternativesException {
        Question question = questionRepository.findById(idQuestion)
                .orElseThrow(() -> new QuestionNotFoundException("A questão não foi encontrada"));

        Alternative alternative = new Alternative(alternativeRequest, question);
        addAlternativeToQuestion(alternative,question);

        alternativeRepository.save(alternative);
        return alternative.entityToResponse();
    }

    private void addAlternativeToQuestion(Alternative alternative, Question question)
            throws LimitOfAlternativesException, AlternativeCorrectDuplicateException, FalseAlternativesOnlyException {
        if (question.isFullListOfAlternatives()){
            throw new LimitOfAlternativesException("Limite máximo de alternativas");
        }
        if (isAlternativeCorrectDuplicate(alternative, question)) {
            throw new AlternativeCorrectDuplicateException("Já existe uma alternativa verdadeira");
        }
        if (isFalseAlternativesOnly(alternative, question)){
            throw new FalseAlternativesOnlyException("Não é possível cadastrar apenas alternativas falsas");
        }

        question.addAlternative(alternative);
    }

    private boolean isAlternativeCorrectDuplicate(Alternative alternative, Question question){
        for (Alternative a : question.getAlternatives()){
            if (a.getCorrect() && alternative.getCorrect()){
                return true;
            }
        }
        return false;
    }

    private boolean isFalseAlternativesOnly(Alternative alternative, Question question){
        int fakeAlternatives = 0;
        for (Alternative a : question.getAlternatives()){
            if (!a.getCorrect() && !alternative.getCorrect()){
                fakeAlternatives++;
            }
        }
        return fakeAlternatives == 3;
    }

    public AlternativeResponse updateAlternative(Long id, AlternativeUpdate alternativeUpdate){
        Alternative alternative = alternativeRepository.findById(id)
                .orElseThrow(() -> new AlternativeNotFoundException("Alternativa não encontrada"));

        updateData(alternative, alternativeUpdate);
        alternativeRepository.save(alternative);

        return alternative.entityToResponse();
    }

    private void updateData(Alternative alternative, AlternativeUpdate alternativeUpdate) {
        alternative.setResponse(alternativeUpdate.response());
    }
}
