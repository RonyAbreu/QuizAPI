package br.ufpb.dcx.apps4society.quizapi.repository;

import br.ufpb.dcx.apps4society.quizapi.entity.Response;
import br.ufpb.dcx.apps4society.quizapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseRepository extends JpaRepository<Response,Long> {
    Page<Response> findByUser(Pageable pageable, User user);
    Page<Response> findByQuestionCreator(Pageable pageable, User creator);
}
