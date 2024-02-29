package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.repository.ThemeRepository;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.security.TokenProvider;
import com.ronyelison.quiz.service.exception.InvalidUserException;
import com.ronyelison.quiz.service.exception.QuestionNotFoundException;
import com.ronyelison.quiz.service.exception.ThemeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;
    private ThemeRepository themeRepository;
    private UserRepository userRepository;
    private TokenProvider tokenProvider;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ThemeRepository themeRepository,
                           UserRepository userRepository, TokenProvider tokenProvider) {
        this.questionRepository = questionRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public QuestionResponse insertQuestion(QuestionRequest questionRequest, Long idTheme, String token) throws InvalidUserException {
        User creator = findUserByToken(token);

        Theme theme = themeRepository.findById(idTheme)
                .orElseThrow(()-> new ThemeNotFoundException("Tema não encontrado"));

        Question question = new Question(questionRequest, theme, creator);
        theme.addQuestion(question);
        creator.addQuestion(question);

        questionRepository.save(question);
        return question.entityToResponse();
    }

    public void removeQuestion(Long id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Questão não encontrada"));

        question.removeQuestionOfThemeList(id);
        questionRepository.delete(question);
    }

    public List<QuestionResponse> findAllQuestions(){
        if (questionRepository.findAll().isEmpty()){
            throw new QuestionNotFoundException("Nenhuma questão foi cadastrada");
        }

        return questionRepository.findAll()
                .stream()
                .map(Question::entityToResponse)
                .toList();
    }

    public QuestionResponse findQuestionById(Long id){
        return questionRepository
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Questão não encontrada"))
                .entityToResponse();
    }

    public List<QuestionResponse> findQuestionByThemesName(String name){
        if (questionRepository.findByThemeNameIgnoreCase(name).isEmpty()){
            throw new QuestionNotFoundException("Não existe nenhum Questão ligada a esse Tema");
        }

        return questionRepository
                .findByThemeNameIgnoreCase(name)
                .stream()
                .map(Question::entityToResponse)
                .toList();
    }

    public QuestionResponse updateQuestion(Long id, QuestionUpdate questionUpdate){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Questão não encontrada"));

        updateData(question, questionUpdate);
        questionRepository.save(question);

        return question.entityToResponse();
    }

    private void updateData(Question question, QuestionUpdate questionUpdate){
        question.setTitle(questionUpdate.title());
        question.setImageUrl(questionUpdate.imageUrl());
    }

    private User findUserByToken(String token) throws InvalidUserException {
        if (token.startsWith("Bearer ")){
            token = token.substring("Bearer ".length());
        }
        String email = tokenProvider.getSubjectByToken(token);

        User user = (User) userRepository.findByEmail(email);

        if (user == null){
            throw new InvalidUserException("Usuário inválido, pode ter sido removido do BD e utilizado o token");
        }

        return user;
    }

}
