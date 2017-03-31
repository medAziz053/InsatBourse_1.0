package tn.insat.bourse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tn.insat.bourse.domain.ActionBourse;

import tn.insat.bourse.repository.ActionBourseRepository;
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
 * REST controller for managing ActionBourse.
 */
@RestController
@RequestMapping("/api")
public class ActionBourseResource {

    private final Logger log = LoggerFactory.getLogger(ActionBourseResource.class);

    @Inject
    private ActionBourseRepository actionBourseRepository;

    /**
     * POST  /action-bourses : Create a new actionBourse.
     *
     * @param actionBourse the actionBourse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actionBourse, or with status 400 (Bad Request) if the actionBourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/action-bourses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActionBourse> createActionBourse(@RequestBody ActionBourse actionBourse) throws URISyntaxException {
        log.debug("REST request to save ActionBourse : {}", actionBourse);
        if (actionBourse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("actionBourse", "idexists", "A new actionBourse cannot already have an ID")).body(null);
        }
        ActionBourse result = actionBourseRepository.save(actionBourse);
        return ResponseEntity.created(new URI("/api/action-bourses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("actionBourse", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /action-bourses : Updates an existing actionBourse.
     *
     * @param actionBourse the actionBourse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actionBourse,
     * or with status 400 (Bad Request) if the actionBourse is not valid,
     * or with status 500 (Internal Server Error) if the actionBourse couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/action-bourses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActionBourse> updateActionBourse(@RequestBody ActionBourse actionBourse) throws URISyntaxException {
        log.debug("REST request to update ActionBourse : {}", actionBourse);
        if (actionBourse.getId() == null) {
            return createActionBourse(actionBourse);
        }
        ActionBourse result = actionBourseRepository.save(actionBourse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("actionBourse", actionBourse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /action-bourses : get all the actionBourses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of actionBourses in body
     */
    @RequestMapping(value = "/action-bourses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActionBourse> getAllActionBourses() {
        log.debug("REST request to get all ActionBourses");
        List<ActionBourse> actionBourses = actionBourseRepository.findAll();
        return actionBourses;
    }

    /**
     * GET  /action-bourses/:id : get the "id" actionBourse.
     *
     * @param id the id of the actionBourse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actionBourse, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/action-bourses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActionBourse> getActionBourse(@PathVariable Long id) {
        log.debug("REST request to get ActionBourse : {}", id);
        ActionBourse actionBourse = actionBourseRepository.findOne(id);
        return Optional.ofNullable(actionBourse)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/action-bourses/wallet/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ActionBourse> getActionBoursesByWalletId(@PathVariable Long id) {
        log.debug("REST request to get all ActionBourses By wallet id");
        List<ActionBourse> actionBourses = actionBourseRepository.findByPortefeuilId(id);
        return actionBourses;
    }

    /**
     * DELETE  /action-bourses/:id : delete the "id" actionBourse.
     *
     * @param id the id of the actionBourse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/action-bourses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActionBourse(@PathVariable Long id) {
        log.debug("REST request to delete ActionBourse : {}", id);
        actionBourseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("actionBourse", id.toString())).build();
    }

}
