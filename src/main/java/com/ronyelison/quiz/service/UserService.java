package com.ronyelison.quiz.service;

import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse insertUser(UserRequest userRequest){
        User user = new User(userRequest);

        //IMPLEMENTAÇÃO DO SPRING SECURITY

        userRepository.save(user);

        return user.entityToResponse();
    }
}
