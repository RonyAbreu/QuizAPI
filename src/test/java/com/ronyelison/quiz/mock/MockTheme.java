package com.ronyelison.quiz.mock;

import com.ronyelison.quiz.dto.theme.ThemeRequest;
import com.ronyelison.quiz.dto.theme.ThemeUpdate;
import com.ronyelison.quiz.entity.Theme;
import com.ronyelison.quiz.entity.User;
import org.springframework.data.domain.Page;

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
    public ThemeRequest mockDTO(Integer num) {
        return new ThemeRequest("Tema");
    }

    public ThemeUpdate mockThemeUpdate(){
        return new ThemeUpdate("Novo tema");
    }
}
