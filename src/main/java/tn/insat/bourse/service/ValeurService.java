package tn.insat.bourse.service;

import tn.insat.bourse.domain.Valeur;
import tn.insat.bourse.repository.ValeurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Valeur.
 */
@Service
@Transactional
public class ValeurService {

    private final Logger log = LoggerFactory.getLogger(ValeurService.class);
    
    @Inject
    private ValeurRepository valeurRepository;

    /**
     * Save a valeur.
     *
     * @param valeur the entity to save
     * @return the persisted entity
     */
    public Valeur save(Valeur valeur) {
        log.debug("Request to save Valeur : {}", valeur);
        Valeur result = valeurRepository.save(valeur);
        return result;
    }

    /**
     *  Get all the valeurs.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Valeur> findAll(Pageable pageable) {
        log.debug("Request to get all Valeurs");
        Page<Valeur> result = valeurRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one valeur by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Valeur findOne(Long id) {
        log.debug("Request to get Valeur : {}", id);
        Valeur valeur = valeurRepository.findOne(id);
        return valeur;
    }

    /**
     *  Delete the  valeur by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Valeur : {}", id);
        valeurRepository.delete(id);
    }
}
