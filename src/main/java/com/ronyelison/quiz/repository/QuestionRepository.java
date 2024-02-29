package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByTheme(Long id);
}
