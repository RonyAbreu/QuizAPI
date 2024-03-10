package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.response.ResponseDTO;
import com.ronyelison.quiz.entity.Alternative;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Response;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.repository.AlternativeRepository;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.repository.ResponseRepository;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.service.exception.AlternativeNotFoundException;
import com.ronyelison.quiz.service.exception.QuestionNotFoundException;
import com.ronyelison.quiz.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ResponseService {
    private ResponseRepository responseRepository;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;
    private AlternativeRepository alternativeRepository;

    @Autowired
    public ResponseService(ResponseRepository responseRepository, UserRepository userRepository,
                           QuestionRepository questionRepository, AlternativeRepository alternativeRepository) {
        this.responseRepository = responseRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.alternativeRepository = alternativeRepository;
    }

    public ResponseDTO insertResponse(UUID userId, Long idQuestion, Long idAlternative){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Question question = questionRepository.findById(idQuestion)
                .orElseThrow(() -> new QuestionNotFoundException("Questão não encontrada"));

        Alternative alternative = alternativeRepository.findById(idAlternative)
                .orElseThrow(() -> new AlternativeNotFoundException("Alternativa não encontrada"));

        Response response = new Response(user,question,alternative);
        user.addResponse(response);

        responseRepository.save(response);

        return response.entityToResponse();
    }
}
