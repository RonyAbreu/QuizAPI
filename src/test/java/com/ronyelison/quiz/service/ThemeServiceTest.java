package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeResponse;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.mock.MockTheme;
import com.ronyelison.quiz.mock.MockUser;
import com.ronyelison.quiz.repository.ThemeRepository;
import com.ronyelison.quiz.service.exception.ThemeAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThemeServiceTest {
    MockTheme mockTheme;
    MockUser mockUser;
    @Mock
    ThemeRepository repository;
    @Mock
    UserService userService;
    @InjectMocks
    ThemeService themeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockTheme = new MockTheme();
        mockUser = new MockUser();
    }

    @Test
    void insertTheme() throws ThemeAlreadyExistsException {
        ThemeRequest themeRequest = mockTheme.mockDTO(1);
        User user = mockUser.mockEntity(1);
        Theme theme = mockTheme.mockEntity(1);

        Mockito.lenient().when(repository.findByNameIgnoreCase("New Theme")).thenReturn(null);
        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(repository.save(theme)).thenReturn(theme);

        ThemeResponse result = themeService.insertTheme(themeRequest, MockUser.MOCK_TOKEN);

        assertNotNull(result);
        assertEquals("Tema", result.name());
    }

    @Test
    void removeTheme() {
        User user = mockUser.mockEntity(1);
        Theme theme = mockTheme.mockEntity(1, user);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(repository.findById(theme.getId())).thenReturn(Optional.of(theme));

        assertDoesNotThrow(() -> themeService.removeTheme(theme.getId(), MockUser.MOCK_TOKEN));
        verify(repository, times(1)).delete(theme);
    }

    @Test
    void findAllThemes() {
        Pageable pageable = mock(Pageable.class);

        List<Theme> themeList = Collections.singletonList(new Theme("TestTheme", new User("userId", "userName", "userToken")));
        Page<Theme> themePage = new PageImpl<>(themeList);
        Mockito.lenient().when(repository.findAll(pageable)).thenReturn(themePage);

        assertDoesNotThrow(() -> themeService.findAllThemes(pageable));
        assertEquals(1, themePage.getNumberOfElements());

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void findThemeById() {
        Theme theme = mockTheme.mockEntity(1);

        Mockito.lenient().when(repository.findById(theme.getId())).thenReturn(Optional.of(theme));

        ThemeResponse result = themeService.findThemeById(theme.getId());

        assertNotNull(result);
        assertEquals("Tema", result.name());
    }

    @Test
    void updateTheme() throws UserNotHavePermissionException {
        ThemeUpdate themeUpdate = mockTheme.mockThemeUpdate();
        User user = mockUser.mockEntity(1);
        Theme theme = mockTheme.mockEntity(1, user);

        Mockito.lenient().when(userService.findUserByToken(MockUser.MOCK_TOKEN)).thenReturn(user);
        Mockito.lenient().when(repository.findById(theme.getId())).thenReturn(Optional.of(theme));

        // Act
        ThemeResponse result = themeService.updateTheme(theme.getId(), themeUpdate, MockUser.MOCK_TOKEN);

        Mockito.lenient().when(repository.save(theme)).thenReturn(theme);

        // Assert
        assertNotNull(result);
        assertEquals("Novo tema", result.name());
    }
}