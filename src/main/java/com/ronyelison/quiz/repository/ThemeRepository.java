package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
