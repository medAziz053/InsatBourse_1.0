package tn.insat.bourse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tn.insat.bourse.domain.UserWallet;
import tn.insat.bourse.service.UserWalletService;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserWallet.
 */
@RestController
@RequestMapping("/api")
public class UserWalletResource {

    private final Logger log = LoggerFactory.getLogger(UserWalletResource.class);
        
    @Inject
    private UserWalletService userWalletService;

    /**
     * POST  /user-wallets : Create a new userWallet.
     *
     * @param userWallet the userWallet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userWallet, or with status 400 (Bad Request) if the userWallet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-wallets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserWallet> createUserWallet(@RequestBody UserWallet userWallet) throws URISyntaxException {
        log.debug("REST request to save UserWallet : {}", userWallet);
        if (userWallet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userWallet", "idexists", "A new userWallet cannot already have an ID")).body(null);
        }
        UserWallet result = userWalletService.save(userWallet);
        return ResponseEntity.created(new URI("/api/user-wallets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userWallet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-wallets : Updates an existing userWallet.
     *
     * @param userWallet the userWallet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userWallet,
     * or with status 400 (Bad Request) if the userWallet is not valid,
     * or with status 500 (Internal Server Error) if the userWallet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-wallets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserWallet> updateUserWallet(@RequestBody UserWallet userWallet) throws URISyntaxException {
        log.debug("REST request to update UserWallet : {}", userWallet);
        if (userWallet.getId() == null) {
            return createUserWallet(userWallet);
        }
        UserWallet result = userWalletService.save(userWallet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userWallet", userWallet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-wallets : get all the userWallets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userWallets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/user-wallets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserWallet>> getAllUserWallets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserWallets");
        Page<UserWallet> page = userWalletService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-wallets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-wallets/:id : get the "id" userWallet.
     *
     * @param id the id of the userWallet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userWallet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-wallets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserWallet> getUserWallet(@PathVariable Long id) {
        log.debug("REST request to get UserWallet : {}", id);
        UserWallet userWallet = userWalletService.findOne(id);
        return Optional.ofNullable(userWallet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-wallets/:id : delete the "id" userWallet.
     *
     * @param id the id of the userWallet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-wallets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserWallet(@PathVariable Long id) {
        log.debug("REST request to delete UserWallet : {}", id);
        userWalletService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userWallet", id.toString())).build();
    }

}
