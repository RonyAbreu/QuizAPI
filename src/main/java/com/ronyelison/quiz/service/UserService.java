package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.user.*;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.security.TokenProvider;
import com.ronyelison.quiz.service.exception.InvalidUserException;
import com.ronyelison.quiz.service.exception.UserAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotFoundException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public UserResponse registerUser(UserRequest userRequest) throws UserAlreadyExistsException {
        User user = (User) userRepository.findByEmail(userRequest.email());

        if (user != null){
            throw new UserAlreadyExistsException("Tente se registrar com outro email");
        }

        User saveUser = new User(userRequest);
        saveUser.setPassword(passwordEncoder.encode(userRequest.password()));

        userRepository.save(saveUser);

        return saveUser.entityToResponse();
    }

    public TokenResponse loginUser(UserLogin userLogin){
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
        Authentication auth = authenticationManager.authenticate(user);
        String token = tokenProvider.generateToken((User) auth.getPrincipal());
        return new TokenResponse(token);
    }

    public void removeUser(UUID id, String token) throws UserNotHavePermissionException {
        User loggedUser = findUserByToken(token);

        User removeUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (loggedUser.userNotHavePermission(removeUser)){
            throw new UserNotHavePermissionException("Você não tem permissão para remover esse usuário");
        }

        userRepository.delete(removeUser);
    }

    public UserResponse updateUser(UUID id, UserUpdate userUpdate, String token) throws UserNotHavePermissionException {
        User loggedUser = findUserByToken(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (loggedUser.userNotHavePermission(user)){
            throw new UserNotHavePermissionException("Você não tem permissão para atualizar esse usuário");
        }

        updateData(user, userUpdate);
        userRepository.save(user);

        return user.entityToResponse();
    }

    private void updateData(User user, UserUpdate userUpdate) {
        user.setName(userUpdate.name());
    }

    public User findUserByToken(String token) {
        if (token.startsWith("Bearer ")){
            token = token.substring("Bearer ".length());
        }
        String email = tokenProvider.getSubjectByToken(token);

        User user = (User) userRepository.findByEmail(email);

        if (user == null){
            throw new InvalidUserException("Usuário inválido, pode ter sido removido do BD e utilizado o token");
        }

        return user;
    }
}
