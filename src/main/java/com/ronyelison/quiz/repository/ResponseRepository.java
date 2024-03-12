package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResponseRepository extends JpaRepository<Response,Long> {
    Page<Response> findByUserUuid(Pageable pageable, UUID uuid);
    Page<Response> findByQuestionCreatorUuid(Pageable pageable, UUID uuid);
}
