package com.ronyelison.quiz.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.dto.user.UserUpdate;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.security.TokenProvider;
import com.ronyelison.quiz.service.exception.UserAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    MockUser mockUser;
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    PasswordEncoder passwordEncoder;
    final String MOCK_TOKEN = "mockToken";
    
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        mockUser = new MockUser();
        ReflectionTestUtils.setField(tokenProvider, "secret", "quizapi-dev");
        ReflectionTestUtils.setField(tokenProvider, "algorithm", Algorithm.HMAC256("quizapi-dev"));
    }

    @Test
    void registerUser() throws UserAlreadyExistsException {
        User user = mockUser.mockEntity(1);
        UserRequest userRequest = mockUser.mockDTO(1);

        Mockito.lenient().when(userRepository.save(user)).thenReturn(user);

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertNotNull(result.email());
        assertNotNull(result.name());
    }

    @Test
    void removeUser() throws UserNotHavePermissionException {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User userLogged = userService.findUserByToken(MOCK_TOKEN);

        Mockito.lenient().when(userRepository.findById(userLogged.getUuid())).thenReturn(Optional.of(userLogged));

        userRepository.delete(userLogged);
        userService.removeUser(userLogged.getUuid(),MOCK_TOKEN);
    }

    @Test
    void updateUser() throws UserNotHavePermissionException {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User userLogged = userService.findUserByToken(MOCK_TOKEN);

        Mockito.lenient().when(userRepository.findById(userLogged.getUuid())).thenReturn(Optional.of(userLogged));

        UserUpdate userUpdate = mockUser.mockUserUpdate();
        UserResponse userResponse = userService.updateUser(userLogged.getUuid(), userUpdate, MOCK_TOKEN);

        assertNotNull(userResponse);
        assertEquals("Novo nome", userResponse.name());
        assertNotNull(userResponse.uuid());
        assertNotNull(userResponse.email());
    }

    @Test
    void findUserByToken() {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User result = userService.findUserByToken(MOCK_TOKEN);

        assertEquals(userMock.getEmail(), result.getEmail());
        assertEquals(userMock.getName(), result.getName());
    }
}