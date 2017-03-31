package tn.insat.bourse.service;

import tn.insat.bourse.domain.UserWallet;
import tn.insat.bourse.repository.UserWalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing UserWallet.
 */
@Service
@Transactional
public class UserWalletService {

    private final Logger log = LoggerFactory.getLogger(UserWalletService.class);
    
    @Inject
    private UserWalletRepository userWalletRepository;

    /**
     * Save a userWallet.
     *
     * @param userWallet the entity to save
     * @return the persisted entity
     */
    public UserWallet save(UserWallet userWallet) {
        log.debug("Request to save UserWallet : {}", userWallet);
        UserWallet result = userWalletRepository.save(userWallet);
        return result;
    }

    /**
     *  Get all the userWallets.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<UserWallet> findAll(Pageable pageable) {
        log.debug("Request to get all UserWallets");
        Page<UserWallet> result = userWalletRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one userWallet by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public UserWallet findOne(Long id) {
        log.debug("Request to get UserWallet : {}", id);
        UserWallet userWallet = userWalletRepository.findOne(id);
        return userWallet;
    }

    /**
     *  Delete the  userWallet by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserWallet : {}", id);
        userWalletRepository.delete(id);
    }
}
