package tn.insat.bourse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tn.insat.bourse.domain.Portefeuil;

import tn.insat.bourse.domain.User;
import tn.insat.bourse.repository.PortefeuilRepository;
import tn.insat.bourse.repository.UserRepository;
import tn.insat.bourse.service.UserService;
import tn.insat.bourse.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Portefeuil.
 */
@RestController
@RequestMapping("/api")
public class PortefeuilResource {

    private final Logger log = LoggerFactory.getLogger(PortefeuilResource.class);

    @Inject
    private PortefeuilRepository portefeuilRepository;


    @Inject
    private UserRepository userRepository;
    /**
     * POST  /portefeuils : Create a new portefeuil.
     *
     * @param portefeuil the portefeuil to create
     * @return the ResponseEntity with status 201 (Created) and with body the new portefeuil, or with status 400 (Bad Request) if the portefeuil has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/portefeuils",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Portefeuil> createPortefeuil(@RequestBody Portefeuil portefeuil) throws URISyntaxException {
        log.debug("REST request to save Portefeuil : {}", portefeuil);
        if (portefeuil.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("portefeuil", "idexists", "A new portefeuil cannot already have an ID")).body(null);
        }
        Portefeuil result = portefeuilRepository.save(portefeuil);
        return ResponseEntity.created(new URI("/api/portefeuils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("portefeuil", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /portefeuils : Updates an existing portefeuil.
     *
     * @param portefeuil the portefeuil to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated portefeuil,
     * or with status 400 (Bad Request) if the portefeuil is not valid,
     * or with status 500 (Internal Server Error) if the portefeuil couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/portefeuils",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Portefeuil> updatePortefeuil(@RequestBody Portefeuil portefeuil) throws URISyntaxException {
        log.debug("REST request to update Portefeuil : {}", portefeuil);
        if (portefeuil.getId() == null) {
            return createPortefeuil(portefeuil);
        }
        Portefeuil result = portefeuilRepository.save(portefeuil);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("portefeuil", portefeuil.getId().toString()))
            .body(result);
    }

    /**
     * GET  /portefeuils : get all the portefeuils.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of portefeuils in body
     */
    @RequestMapping(value = "/portefeuils",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Portefeuil> getAllPortefeuils() {
        log.debug("REST request to get all Portefeuils");
        List<Portefeuil> portefeuils = portefeuilRepository.findAll();
        return portefeuils;
    }

    /**
     * GET  /portefeuils/:id : get the "id" portefeuil.
     *
     * @param id the id of the portefeuil to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the portefeuil, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/portefeuils/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Portefeuil> getPortefeuil(@PathVariable Long id) {
        log.debug("REST request to get Portefeuil : {}", id);
        Portefeuil portefeuil = portefeuilRepository.findOne(id);
        return Optional.ofNullable(portefeuil)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /portefeuils/:id : delete the "id" portefeuil.
     *
     * @param id the id of the portefeuil to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/portefeuils/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePortefeuil(@PathVariable Long id) {
        log.debug("REST request to delete Portefeuil : {}", id);
        portefeuilRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("portefeuil", id.toString())).build();
    }

    @RequestMapping(value = "/portfeuils/user/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Portefeuil> getPortefeuilByUser(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a portefeuil by its user id");
        User user = userRepository.findOne(id);
        Portefeuil portefeuil = portefeuilRepository.findByUser(user);
        return Optional.ofNullable(portefeuil)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
