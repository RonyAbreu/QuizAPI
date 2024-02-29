package com.ronyelison.quiz.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ronyelison.quiz.entity.User;
import com.ronyelison.quiz.service.exception.TokenException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
public class TokenProvider {
    @Value("${app.secret}")
    private String secret;
    private Algorithm algorithm;

    @PostConstruct
    public void setUp(){
        Base64.getEncoder().encode(secret.getBytes());
        algorithm = Algorithm.HMAC256(secret.getBytes());
    }

    public String generateToken(User user){
        try {
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(expirationToken())
                    .sign(algorithm)
                    .strip();
        } catch (JWTCreationException e){
            throw new TokenException(e.getMessage(), e.getCause());
        }
    }

    private Instant expirationToken() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubjectByToken(String token){
        try {
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e){
            throw new TokenException(e.getMessage(), e.getCause());
        }
    }
}
