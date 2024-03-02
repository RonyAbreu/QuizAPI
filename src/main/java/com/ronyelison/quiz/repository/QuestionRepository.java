package com.ronyelison.quiz.repository;

import com.ronyelison.quiz.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    Page<Question> findByThemeId(Long id, Pageable pageable);

    @Query(nativeQuery = true,
    value = "SELECT q.id AS question_id, q.title AS question_title, q.image_url AS question_image_url,\n" +
            "       t.id AS theme_id, t.name AS theme_name,\n" +
            "       a.id AS alternative_id, a.text AS alternative_text, a.correct AS alternative_correct\n" +
            "FROM tb_question q\n" +
            "JOIN tb_theme t ON q.theme_id = t.id\n" +
            "LEFT JOIN tb_alternative a ON q.id = a.question_id\n" +
            "WHERE t.id = :idTheme\n" +
            "ORDER BY RAND()\n" +
            "LIMIT 10")
    List<Question> find10QuestionsByThemeId(Long idTheme);
}
