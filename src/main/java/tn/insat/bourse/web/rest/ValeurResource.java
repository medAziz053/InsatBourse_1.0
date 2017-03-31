package tn.insat.bourse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tn.insat.bourse.domain.Valeur;
import tn.insat.bourse.service.ValeurService;
import tn.insat.bourse.web.rest.util.HeaderUtil;
import tn.insat.bourse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Valeur.
 */
@RestController
@RequestMapping("/api")
public class ValeurResource {

    private final Logger log = LoggerFactory.getLogger(ValeurResource.class);
        
    @Inject
    private ValeurService valeurService;

    /**
     * POST  /valeurs : Create a new valeur.
     *
     * @param valeur the valeur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new valeur, or with status 400 (Bad Request) if the valeur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/valeurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Valeur> createValeur(@Valid @RequestBody Valeur valeur) throws URISyntaxException {
        log.debug("REST request to save Valeur : {}", valeur);
        if (valeur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("valeur", "idexists", "A new valeur cannot already have an ID")).body(null);
        }
        Valeur result = valeurService.save(valeur);
        return ResponseEntity.created(new URI("/api/valeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("valeur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /valeurs : Updates an existing valeur.
     *
     * @param valeur the valeur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated valeur,
     * or with status 400 (Bad Request) if the valeur is not valid,
     * or with status 500 (Internal Server Error) if the valeur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/valeurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Valeur> updateValeur(@Valid @RequestBody Valeur valeur) throws URISyntaxException {
        log.debug("REST request to update Valeur : {}", valeur);
        if (valeur.getId() == null) {
            return createValeur(valeur);
        }
        Valeur result = valeurService.save(valeur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("valeur", valeur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /valeurs : get all the valeurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of valeurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/valeurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Valeur>> getAllValeurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Valeurs");
        Page<Valeur> page = valeurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/valeurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /valeurs/:id : get the "id" valeur.
     *
     * @param id the id of the valeur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the valeur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/valeurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Valeur> getValeur(@PathVariable Long id) {
        log.debug("REST request to get Valeur : {}", id);
        Valeur valeur = valeurService.findOne(id);
        return Optional.ofNullable(valeur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /valeurs/:id : delete the "id" valeur.
     *
     * @param id the id of the valeur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/valeurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteValeur(@PathVariable Long id) {
        log.debug("REST request to delete Valeur : {}", id);
        valeurService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("valeur", id.toString())).build();
    }

}
