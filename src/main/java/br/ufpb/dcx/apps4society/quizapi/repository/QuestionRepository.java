package br.ufpb.dcx.apps4society.quizapi.repository;

import br.ufpb.dcx.apps4society.quizapi.entity.User;
import br.ufpb.dcx.apps4society.quizapi.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    Page<Question> findByThemeId(Long id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM tb_question\n" +
            "WHERE theme_id = :idTheme\n" +
            "ORDER BY RAND()\n" +
            "LIMIT 10;")
    List<Question> find10QuestionsByThemeId(Long idTheme);

    Page<Question> findByCreator(User user, Pageable pageable);
}