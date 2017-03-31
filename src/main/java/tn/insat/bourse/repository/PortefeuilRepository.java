package tn.insat.bourse.repository;

import tn.insat.bourse.domain.Portefeuil;

import org.springframework.data.jpa.repository.*;
import tn.insat.bourse.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Portefeuil entity.
 */
@SuppressWarnings("unused")
public interface PortefeuilRepository extends JpaRepository<Portefeuil,Long> {

    Portefeuil findByUser(User user);

}
