package tn.insat.bourse.repository;

import tn.insat.bourse.domain.Valeur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Valeur entity.
 */
@SuppressWarnings("unused")
public interface ValeurRepository extends JpaRepository<Valeur,Long> {

}
