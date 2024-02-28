package com.ronyelison.quiz.entity;

import com.ronyelison.quiz.dto.user.UserRequest;
import com.ronyelison.quiz.dto.user.UserResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private String email;
    private String password;

    public User(){

    }

    public User(UserRequest userRequest) {
        this.name = userRequest.name();
        this.email = userRequest.email();
        this.password = userRequest.password();
    }

    public UserResponse entityToResponse(){
        return new UserResponse(uuid,name,email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
