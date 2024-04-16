package br.ufpb.dcx.apps4society.quizapi.repository;

import br.ufpb.dcx.apps4society.quizapi.entity.Theme;
import br.ufpb.dcx.apps4society.quizapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findByNameIgnoreCase(String name);
    Page<Theme> findByNameStartsWithIgnoreCase(String name, Pageable pageable);
    Page<Theme> findByCreator(User creator, Pageable pageable);
}
