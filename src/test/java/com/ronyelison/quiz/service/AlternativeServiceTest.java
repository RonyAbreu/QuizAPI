package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.alternative.AlternativeRequest;
import com.ronyelison.quiz.dto.alternative.AlternativeResponse;
import com.ronyelison.quiz.dto.alternative.AlternativeUpdate;
import com.ronyelison.quiz.entity.Alternative;
import com.ronyelison.quiz.entity.Question;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.mock.MockAlternative;
import com.ronyelison.quiz.mock.MockQuestion;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.repository.AlternativeRepository;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.service.exception.AlternativeCorrectDuplicateException;
import com.ronyelison.quiz.service.exception.FalseAlternativesOnlyException;
import com.ronyelison.quiz.service.exception.LimitOfAlternativesException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlternativeServiceTest {
    MockAlternative mockAlternative;
    MockQuestion mockQuestion;
    MockUser mockUser;
    @Mock
    AlternativeRepository alternativeRepository;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    UserService userService;
    @InjectMocks
    AlternativeService alternativeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAlternative = new MockAlternative();
        mockQuestion = new MockQuestion();
        mockUser = new MockUser();
    }

    @Test
    void insertAlternative() throws FalseAlternativesOnlyException, AlternativeCorrectDuplicateException, LimitOfAlternativesException {
        Alternative alternative = mockAlternative.mockEntity(1);

        AlternativeRequest alternativeRequest = mockAlternative.mockDTO(1);
        Question question = mockQuestion.mockEntity(1, new Theme(), new User());

        Mockito.lenient().when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Mockito.lenient().when(alternativeRepository.save(alternative)).thenReturn(alternative);

        AlternativeResponse result = alternativeService.insertAlternative(alternativeRequest, question.getId());

        assertNotNull(result);
        assertEquals("Alternative", result.response());
    }


    @Test
    void updateAlternative() throws UserNotHavePermissionException {
        AlternativeUpdate alternativeUpdate = mockAlternative.mockAlternativeUpdate();
        User user = mockUser.mockEntity(1);
        Question question = mockQuestion.mockEntity(1, new Theme(), user);
        Alternative alternative = mockAlternative.mockEntity(1, false, question);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(alternativeRepository.findById(alternative.getId())).thenReturn(Optional.of(alternative));
        Mockito.lenient().when(alternativeRepository.save(alternative)).thenReturn(alternative);

        AlternativeResponse result = alternativeService.updateAlternative(alternative.getId(), alternativeUpdate, MockUser.MOCK_TOKEN);

        assertNotNull(result);
        assertEquals("Nova Alternativa", result.response());
    }
}