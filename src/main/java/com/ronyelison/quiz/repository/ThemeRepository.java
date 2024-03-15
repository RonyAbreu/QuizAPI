package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Theme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findByNameIgnoreCase(String name);
    Page<Theme> findByNameStartsWithIgnoreCase(String name, Pageable pageable);
}
