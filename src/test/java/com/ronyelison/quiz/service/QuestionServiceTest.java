package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.question.QuestionRequest;
import com.ronyelison.quiz.dto.question.QuestionResponse;
import com.ronyelison.quiz.dto.question.QuestionUpdate;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.mock.MockQuestion;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.repository.ThemeRepository;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class QuestionServiceTest {
    MockQuestion mockQuestion;
    MockUser mockUser;
    MockTheme mockTheme;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    ThemeRepository themeRepository;
    @Mock
    UserService userService;
    @InjectMocks
    QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockQuestion = new MockQuestion();
        mockUser = new MockUser();
        mockTheme = new MockTheme();
    }

    @Test
    void insertQuestion() {
        QuestionRequest questionRequest = mockQuestion.mockDTO(1);
        User creator = mockUser.mockEntity(1);
        Theme theme = mockTheme.mockEntity(1);
        Question question = mockQuestion.mockEntity(1);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(creator);
        Mockito.lenient().when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));
        Mockito.lenient().when(questionRepository.save(question)).thenReturn(question);

        QuestionResponse result = questionService.insertQuestion(questionRequest, theme.getId(), MockUser.MOCK_TOKEN);

        assertNotNull(result);
        assertNotNull(result.imageUrl());
        assertEquals("Question", result.title());
    }

    @Test
    void removeQuestion() {
        User user = mockUser.mockEntity(1);
        Question question = mockQuestion.mockEntity(1, new Theme(), user);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        assertDoesNotThrow(() -> questionService.removeQuestion(question.getId(), MockUser.MOCK_TOKEN));

        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void findAllQuestions() {
        Pageable pageable = mock(Pageable.class);

        List<Question> questionList = mockQuestion.mockList(5);
        Page<Question> questionPage = new PageImpl<>(questionList);
        Mockito.lenient().when(questionRepository.findAll(pageable)).thenReturn(questionPage);

        List<QuestionResponse> result = questionService.findAllQuestions(pageable).toList();

        assertEquals(5, result.size());
        assertEquals("Question", result.getFirst().title());
    }

    @Test
    void find10QuestionsByThemeId() {
        Theme theme = mockTheme.mockEntity(1);
        List<Question> questionList = mockQuestion.mockList(10);
        Mockito.lenient().when(questionRepository.find10QuestionsByThemeId(theme.getId())).thenReturn(questionList);
        List<QuestionResponse> result = questionService.find10QuestionsByThemeId(theme.getId());

        assertEquals(10, result.size());
        assertEquals("Question", result.getFirst().title());
    }

    @Test
    void findQuestionById() {
        Question question = mockQuestion.mockEntity(1);
        Mockito.lenient().when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        QuestionResponse result = questionService.findQuestionById(question.getId());

        assertNotNull(result);
        assertNotNull(result.imageUrl());
        assertEquals("Question", result.title());
    }

    @Test
    void findQuestionByThemeId() {
        Pageable pageable = mock(Pageable.class);
        Theme theme = mockTheme.mockEntity(1);

        List<Question> questionList = mockQuestion.mockList(5);
        Page<Question> questionPage = new PageImpl<>(questionList);

        Mockito.lenient().when(questionRepository.findByThemeId(theme.getId(), pageable)).thenReturn(questionPage);

        List<QuestionResponse> result = questionService.findQuestionByThemeId(theme.getId(), pageable).toList();

        assertEquals(5, result.size());
        assertNotNull(result.getFirst().imageUrl());
        assertEquals("Question", result.getFirst().title());
    }

    @Test
    void updateQuestion() throws UserNotHavePermissionException {
        QuestionUpdate questionUpdate = mockQuestion.mockQuestionUpdate();
        User user = mockUser.mockEntity(1);
        Question question = mockQuestion.mockEntity(1,new Theme(), user);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Mockito.lenient().when(questionRepository.save(question)).thenReturn(question);

        QuestionResponse result = questionService.updateQuestion(question.getId(), questionUpdate, MockUser.MOCK_TOKEN);

        assertNotNull(result);
        assertEquals("Novo titulo", result.title());
        assertEquals("Nova url", result.imageUrl());
    }
}