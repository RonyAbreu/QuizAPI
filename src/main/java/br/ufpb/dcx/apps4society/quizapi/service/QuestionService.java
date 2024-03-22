package br.ufpb.dcx.apps4society.quizapi.service;

import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionUpdate;
import br.ufpb.dcx.apps4society.quizapi.entity.User;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.question.QuestionResponse;
import br.ufpb.dcx.apps4society.quizapi.entity.Question;
import br.ufpb.dcx.apps4society.quizapi.entity.Theme;
import br.ufpb.dcx.apps4society.quizapi.repository.QuestionRepository;
import br.ufpb.dcx.apps4society.quizapi.repository.ThemeRepository;
import br.ufpb.dcx.apps4society.quizapi.service.exception.QuestionNotFoundException;
import br.ufpb.dcx.apps4society.quizapi.service.exception.ThemeNotFoundException;
import br.ufpb.dcx.apps4society.quizapi.service.exception.UserNotHavePermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;
    private ThemeRepository themeRepository;
    private UserService userService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ThemeRepository themeRepository,
                           UserService userService) {
        this.questionRepository = questionRepository;
        this.themeRepository = themeRepository;
        this.userService = userService;
    }

    public QuestionResponse insertQuestion(QuestionRequest questionRequest, Long idTheme, String token){
        User creator = userService.findUserByToken(token);

        Theme theme = themeRepository.findById(idTheme)
                .orElseThrow(()-> new ThemeNotFoundException("Tema não encontrado"));

        Question question = new Question(questionRequest, theme, creator);
        theme.addQuestion(question);
        creator.addQuestion(question);

        questionRepository.save(question);
        return question.entityToResponse();
    }

    public void removeQuestion(Long id, String token) throws UserNotHavePermissionException {
        User user = userService.findUserByToken(token);
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("A questão não foi encontrada"));

        if (user.userNotHavePermission(question.getCreator())){
            throw new UserNotHavePermissionException("Usuário não tem permissão para remover essa questão");
        }

        question.removeQuestionOfThemeList(id);
        questionRepository.delete(question);
    }

    public Page<QuestionResponse> findAllQuestions(Pageable pageable){
        Page<Question> questionPage = questionRepository.findAll(pageable);

        if (questionPage.isEmpty()){
            throw new QuestionNotFoundException("Nenhuma questão foi cadastrada");
        }

        return questionPage.map(Question::entityToResponse);
    }

    public List<QuestionResponse> find10QuestionsByThemeId(Long id){
        List<Question> questions = questionRepository.find10QuestionsByThemeId(id);

        if (questions.isEmpty()){
            throw new QuestionNotFoundException("Não existe nenhuma Questão ligada a esse Tema");
        }

        return questions.stream().map(Question::entityToResponse).toList();
    }

    public QuestionResponse findQuestionById(Long id){
        return questionRepository
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("A questão não foi encontrada"))
                .entityToResponse();
    }

    public Page<QuestionResponse> findQuestionByThemeId(Long id, Pageable pageable){
        Page<Question> questionPage = questionRepository.findByThemeId(id, pageable);

        if (questionPage.isEmpty()){
            throw new QuestionNotFoundException("Não existe nenhuma Questão ligada a esse Tema");
        }

        return questionPage.map(Question::entityToResponse);
    }

    public Page<QuestionResponse> findQuestionsByCreator(String token, Pageable pageable){
        User creator = userService.findUserByToken(token);

        Page<Question> questions = questionRepository.findByCreator(creator, pageable);

        if (questions.isEmpty()){
            throw new QuestionNotFoundException("Não existe Questões criadas por esse Usuário");
        }

        return questions.map(Question::entityToResponse);
    }

    public QuestionResponse updateQuestion(Long id, QuestionUpdate questionUpdate, String token) throws UserNotHavePermissionException {
        User user = userService.findUserByToken(token);

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("A questão não foi encontrada"));

        if (user.userNotHavePermission(question.getCreator())){
            throw new UserNotHavePermissionException("Usuário não tem permissão para atualizar essa questão");
        }

        updateData(question, questionUpdate);
        questionRepository.save(question);

        return question.entityToResponse();
    }

    private void updateData(Question question, QuestionUpdate questionUpdate){
        question.setTitle(questionUpdate.title());
        question.setImageUrl(questionUpdate.imageUrl());
    }

}
