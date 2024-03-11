package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.response.ResponseDTO;
import com.ronyelison.quiz.entity.*;
import com.ronyelison.quiz.mock.*;
import com.ronyelison.quiz.repository.AlternativeRepository;
import com.ronyelison.quiz.repository.QuestionRepository;
import com.ronyelison.quiz.repository.ResponseRepository;
import com.ronyelison.quiz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ResponseServiceTest {
    MockUser mockUser;
    MockQuestion mockQuestion;
    MockTheme mockTheme;
    MockAlternative mockAlternative;
    MockResponse mockResponse;
    @Mock
    ResponseRepository responseRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    QuestionRepository questionRepository;
    @Mock
    AlternativeRepository alternativeRepository;
    @InjectMocks
    ResponseService responseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new MockUser();
        mockQuestion = new MockQuestion();
        mockTheme = new MockTheme();
        mockAlternative = new MockAlternative();
        mockResponse = new MockResponse();
    }

    @Test
    void insertResponse() {
        User user = mockUser.mockEntity(1);
        Question question = mockQuestion.mockEntity(1, new Theme(), user);
        Alternative alternative = mockAlternative.mockEntity(1, true, question);

        Mockito.lenient().when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));
        Mockito.lenient().when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        Mockito.lenient().when(alternativeRepository.findById(alternative.getId())).thenReturn(Optional.of(alternative));

        ResponseDTO result = responseService.insertResponse(user.getUuid(), question.getId(), alternative.getId());

        assertNotNull(result);
        assertNotNull(result.user());
        assertNotNull(result.question());
        assertNotNull(result.alternative());
    }

    @Test
    void findAllResponses() {
        Pageable pageable = mock(Pageable.class);

        List<Response> responseList = mockResponse.mockList(5);
        Page<Response> responsePage = new PageImpl<>(responseList);
        Mockito.lenient().when(responseRepository.findAll(pageable)).thenReturn(responsePage);

        Page<ResponseDTO> result = responseService.findAllResponses(pageable);

        assertEquals(responseList.size(), result.getTotalElements());
    }

    @Test
    void findResponsesByUser() {
        User user = mockUser.mockEntity(1);
        Pageable pageable = mock(Pageable.class);

        List<Response> responseList = mockResponse.mockList(5);
        Page<Response> responsePage = new PageImpl<>(responseList);
        Mockito.lenient().when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));
        Mockito.lenient().when(responseRepository.findByUserUuid(pageable, user.getUuid())).thenReturn(responsePage);

        Page<ResponseDTO> result = responseService.findResponsesByUser(pageable, user.getUuid());

        assertEquals(responseList.size(), result.getTotalElements());
    }
}