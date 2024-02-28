package com.ronyelison.quiz.security;

import com.ronyelison.quiz.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private TokenProvider tokenProvider;
    private UserRepository userRepository;

    @Autowired
    public SecurityFilter(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenOfRequest(request);

        if (token != null){
            String email = tokenProvider.getSubjectByToken(token);
            UserDetails userDetails = userRepository.findByEmail(email);
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(user);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenOfRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null){
            return null;
        }

        return token.substring("Bearer ".length());
    }
}
