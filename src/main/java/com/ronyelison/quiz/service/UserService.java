package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.user.TokenResponse;
import com.ronyelison.quiz.dto.user.UserLogin;
import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.repository.UserRepository;
import com.ronyelison.quiz.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserResponse registerUser(UserRequest userRequest){
        User user = new User(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userRepository.save(user);

        return user.entityToResponse();
    }

    public TokenResponse loginUser(UserLogin userLogin){
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userLogin.email(), userLogin.password());
        Authentication auth = authenticationManager.authenticate(user);
        String token = tokenProvider.generateToken((User) auth.getPrincipal());
        return new TokenResponse(token);
    }
}
