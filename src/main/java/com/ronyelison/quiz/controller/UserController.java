package com.ronyelison.quiz.controller;

import com.ronyelison.quiz.dto.user.*;
import com.ronyelison.quiz.service.UserService;
import com.ronyelison.quiz.service.exception.UserAlreadyExistsException;
import com.ronyelison.quiz.service.exception.UserNotHavePermissionException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest) throws UserAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRequest));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid UserLogin userLogin){
        return ResponseEntity.ok(userService.loginUser(userLogin));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable UUID id, @RequestHeader("Authorization") String token) throws UserNotHavePermissionException {
        userService.removeUser(id, token);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID id, @RequestBody @Valid UserUpdate userUpdate,
                                                   @RequestHeader("Authorization") String token) throws UserNotHavePermissionException {
        return ResponseEntity.ok(userService.updateUser(id, userUpdate, token));
    }
}
