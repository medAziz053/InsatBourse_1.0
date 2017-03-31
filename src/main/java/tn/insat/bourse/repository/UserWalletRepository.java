package tn.insat.bourse.repository;

import tn.insat.bourse.domain.UserWallet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserWallet entity.
 */
@SuppressWarnings("unused")
public interface UserWalletRepository extends JpaRepository<UserWallet,Long> {

}
