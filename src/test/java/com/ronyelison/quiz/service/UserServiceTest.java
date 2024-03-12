package com.ronyelison.quiz.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.dto.user.UserUpdate;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.security.TokenProvider;
import com.ronyelison.quiz.service.exception.InvalidUserException;
import com.ronyelison.quiz.service.exception.UserAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotFoundException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import com.ronyelison.quiz.util.Messages;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    void registerUserAlreadyExists() {
        User user = mockUser.mockEntity(1);
        UserRequest userRequest = mockUser.mockDTO(1);

        Mockito.lenient().when(userRepository.findByEmail(userRequest.email())).thenReturn(user);

        Exception e = assertThrows(UserAlreadyExistsException.class, () ->{
            userService.registerUser(userRequest);
        });

        assertEquals(e.getMessage(), Messages.USER_ALREADY_EXISTS_MESSAGE);
    }

    @Test
    void removeUser() throws UserNotHavePermissionException {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User userLogged = userService.findUserByToken(MockUser.MOCK_TOKEN);

        Mockito.lenient().when(userRepository.findById(userLogged.getUuid())).thenReturn(Optional.of(userLogged));

        userService.removeUser(userLogged.getUuid(),MockUser.MOCK_TOKEN);
        verify(userRepository, times(1)).delete(userLogged);
    }

    @Test
    void removeUserNotFound(){
        User userMock = mockUser.mockEntity(1);
        User userFalse = mockUser.mockEntity(2);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock);

        Mockito.lenient().when(userRepository.findById(userMock.getUuid())).thenReturn(Optional.of(userMock));


        Exception e = assertThrows(UserNotFoundException.class, () ->{
            userService.removeUser(userFalse.getUuid(),MockUser.MOCK_TOKEN);
        });

        assertEquals(e.getMessage(), Messages.USER_NOT_FOUND_MESSAGE);
    }

    @Test
    void removeUserNotHavePermission() {
        User userMock = mockUser.mockEntity(1);
        User userFalse = mockUser.mockEntity(2);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userFalse.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userFalse.getEmail())).thenReturn(userFalse);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(userFalse);

        Mockito.lenient().when(userRepository.findById(userMock.getUuid())).thenReturn(Optional.of(userMock));

        Exception e = assertThrows(UserNotHavePermissionException.class, () ->{
            userService.removeUser(userMock.getUuid(),MockUser.MOCK_TOKEN);
        });

        assertEquals(e.getMessage(), Messages.USER_NOT_HAVE_PERMISSION_FOR_REMOVE);
    }

    @Test
    void updateUser() throws UserNotHavePermissionException {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User userLogged = userService.findUserByToken(MockUser.MOCK_TOKEN);

        Mockito.lenient().when(userRepository.findById(userLogged.getUuid())).thenReturn(Optional.of(userLogged));

        UserUpdate userUpdate = mockUser.mockUserUpdate();
        UserResponse userResponse = userService.updateUser(userLogged.getUuid(), userUpdate, MockUser.MOCK_TOKEN);

        assertNotNull(userResponse);
        assertEquals("Novo nome", userResponse.name());
        assertNotNull(userResponse.uuid());
        assertNotNull(userResponse.email());
    }

    @Test
    void updateUserNotFound() {
        User userMock = mockUser.mockEntity(1);
        User userFalse = mockUser.mockEntity(2);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User userLogged = userService.findUserByToken(MockUser.MOCK_TOKEN);

        Mockito.lenient().when(userRepository.findById(userLogged.getUuid())).thenReturn(Optional.of(userLogged));

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        Exception e = assertThrows(UserNotFoundException.class, () ->{
            userService.updateUser(userFalse.getUuid(), userUpdate, MockUser.MOCK_TOKEN);
        });

        assertEquals(e.getMessage(), Messages.USER_NOT_FOUND_MESSAGE);

    }

    @Test
    void updateUserNotHavePermission() {
        User userMock = mockUser.mockEntity(1);
        User userFalse = mockUser.mockEntity(2);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock);

        Mockito.lenient().when(userRepository.findById(userFalse.getUuid())).thenReturn(Optional.of(userFalse));

        UserUpdate userUpdate = mockUser.mockUserUpdate();

        Exception e = assertThrows(UserNotHavePermissionException.class, () ->{
            userService.updateUser(userFalse.getUuid(), userUpdate, MockUser.MOCK_TOKEN);
        });

        assertEquals(e.getMessage(), Messages.USER_NOT_HAVE_PERMISSION_FOR_UPDATE);

    }

    @Test
    void findUserByToken() {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(userMock);

        User result = userService.findUserByToken(MockUser.MOCK_TOKEN);

        assertEquals(userMock.getEmail(), result.getEmail());
        assertEquals(userMock.getName(), result.getName());
    }

    @Test
    void findInvalidUserByToken() {
        User userMock = mockUser.mockEntity(1);

        Mockito.lenient().when(tokenProvider.getSubjectByToken(MockUser.MOCK_TOKEN)).thenReturn(userMock.getEmail());

        Mockito.lenient().when(userRepository.findByEmail(userMock.getEmail())).thenReturn(null);

        Exception e = assertThrows(InvalidUserException.class, () ->{
            userService.findUserByToken(MockUser.MOCK_TOKEN);
        });

        assertEquals(e.getMessage(), Messages.INVALID_USER_MESSAGE);
    }
}