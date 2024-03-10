package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response,Long> {
}
