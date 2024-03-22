package br.ufpb.dcx.apps4society.quizapi.mock;

import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.theme.ThemeUpdate;
import br.ufpb.dcx.apps4society.quizapi.entity.Theme;
import br.ufpb.dcx.apps4society.quizapi.entity.User;

public class MockTheme implements MockInterface<Theme, ThemeRequest>{

    public Theme mockEntity(Integer num, User user) {
        return new Theme(num.longValue(),
                "Tema",
                user
        );
    }
    @Override
    public Theme mockEntity(Integer num) {
        return new Theme(num.longValue(),
                "Tema",
                new User()
        );
    }

    @Override
    public ThemeRequest mockRequest(Integer num) {
        return new ThemeRequest("Tema");
    }

    public ThemeUpdate mockThemeUpdate(){
        return new ThemeUpdate("Novo tema");
    }
}
