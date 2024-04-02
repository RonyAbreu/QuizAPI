package br.ufpb.dcx.apps4society.quizapi.service;

import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeUpdate;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeResponse;
import br.ufpb.dcx.apps4society.quizapi.entity.Theme;
import br.ufpb.dcx.apps4society.quizapi.entity.User;
import br.ufpb.dcx.apps4society.quizapi.repository.ThemeRepository;
import br.ufpb.dcx.apps4society.quizapi.service.exception.ThemeAlreadyExistsException;
import br.ufpb.dcx.apps4society.quizapi.service.exception.ThemeNotFoundException;
import br.ufpb.dcx.apps4society.quizapi.service.exception.UserNotHavePermissionException;
import br.ufpb.dcx.apps4society.quizapi.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {
    private ThemeRepository repository;
    private UserService userService;

    @Autowired
    public ThemeService(ThemeRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public ThemeResponse insertTheme(ThemeRequest themeRequest, String token) throws ThemeAlreadyExistsException {
        Theme theme = repository.findByNameIgnoreCase(themeRequest.name());

        if (theme != null){
            throw new ThemeAlreadyExistsException("Esse tema já foi cadastrado, tente novamente com outro Nome");
        }

        User user = userService.findUserByToken(token);

        Theme saveTheme = new Theme(themeRequest.name(),themeRequest.imageUrl(), user);
        user.addTheme(saveTheme);

        repository.save(saveTheme);
        return saveTheme.entityToResponse();
    }

    public void removeTheme(Long id, String token) throws UserNotHavePermissionException {
        User user = userService.findUserByToken(token);

        Theme theme = repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException(Messages.THEME_NOT_FOUND));

        if (user.userNotHavePermission(theme.getCreator())) {
            throw new UserNotHavePermissionException("Usuário não tem permissão para remover esse tema");
        }

        repository.delete(theme);
    }

    public Page<ThemeResponse> findAllThemes(Pageable pageable){
        Page<Theme> themes = repository.findAll(pageable);
        if (themes.isEmpty()){
            throw new ThemeNotFoundException("Nenhum tema foi cadastrado");
        }

        return themes.map(Theme::entityToResponse);
    }

    public ThemeResponse findThemeById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException(Messages.THEME_NOT_FOUND))
                .entityToResponse();
    }

    public Page<ThemeResponse> findThemesByName(String name, Pageable pageable){
        Page<Theme> themes = repository.findByNameStartsWithIgnoreCase(name, pageable);

        if (themes.isEmpty()){
            throw new ThemeNotFoundException("Nenhum tema foi cadastrado com esse nome");
        }

        return themes.map(Theme::entityToResponse);
    }

    public ThemeResponse updateTheme(Long id, ThemeUpdate themeUpdate, String token) throws UserNotHavePermissionException, ThemeAlreadyExistsException {
        User user = userService.findUserByToken(token);

        Theme theme = repository.findById(id)
                .orElseThrow(() -> new ThemeNotFoundException(Messages.THEME_NOT_FOUND));

        if (user.userNotHavePermission(theme.getCreator())){
            throw new UserNotHavePermissionException("Usuário não tem permissão para atualizar esse tema");
        }

        Theme themeTestName = repository.findByNameIgnoreCase(themeUpdate.name());

        if (themeTestName != null){
            throw new ThemeAlreadyExistsException("Esse tema já foi cadastrado, tente novamente com outro Nome");
        }

        updateData(theme, themeUpdate);
        repository.save(theme);

        return theme.entityToResponse();
    }

    private void updateData(Theme theme, ThemeUpdate themeUpdate){
        theme.setName(themeUpdate.name());
        theme.setImageUrl(themeUpdate.imageUrl());
    }
}
