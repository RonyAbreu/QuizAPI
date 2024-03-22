package br.ufpb.dcx.apps4society.quizapi.repository;

import br.ufpb.dcx.apps4society.quizapi.entity.Alternative;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativeRepository extends JpaRepository<Alternative,Long> {
}
