package tn.insat.bourse.repository;

import tn.insat.bourse.domain.ActionBourse;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActionBourse entity.
 */
@SuppressWarnings("unused")
public interface ActionBourseRepository extends JpaRepository<ActionBourse,Long> {

    List<ActionBourse> findByPortefeuilId(Long portefeuilId);

}
