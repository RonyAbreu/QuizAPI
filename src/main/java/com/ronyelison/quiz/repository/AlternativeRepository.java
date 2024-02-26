package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Alternative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativeRepository extends JpaRepository<Alternative,Long> {
}
