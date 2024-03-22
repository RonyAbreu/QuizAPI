package br.ufpb.dcx.apps4society.quizapi.mock;

import br.ufpb.dcx.apps4society.quizapi.dto.user.UserLogin;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserRequest;
import br.ufpb.dcx.apps4society.quizapi.dto.user.UserUpdate;
import br.ufpb.dcx.apps4society.quizapi.entity.User;
import br.ufpb.dcx.apps4society.quizapi.entity.enums.Role;

import java.util.UUID;

public class MockUser implements MockInterface<User, UserRequest>{
    public static final String MOCK_TOKEN = "mockToken";

    @Override
    public User mockEntity(Integer num) {
        return new User(
                UUID.randomUUID(),
                "User" + num,
                "user@gmail.com",
                "12345678",
                Role.USER
        );
    }

    @Override
    public UserRequest mockRequest(Integer num) {
        return new UserRequest(
                "User" + num,
                "user@gmail.com",
                "12345678"
        );
    }

    public UserUpdate mockUserUpdate(){
        return new UserUpdate("Novo nome");
    }

    public UserLogin mockUserLogin(){
        return new UserLogin("user@gmail.com", "12345678");
    }
}
