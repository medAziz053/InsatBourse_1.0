package tn.insat.bourse.web.rest;

import tn.insat.bourse.InsatBourseApp;

import tn.insat.bourse.domain.UserWallet;
import tn.insat.bourse.repository.UserWalletRepository;
import tn.insat.bourse.service.UserWalletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserWalletResource REST controller.
 *
 * @see UserWalletResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatBourseApp.class)
public class UserWalletResourceIntTest {

    private static final Float DEFAULT_BALANCE = 1F;
    private static final Float UPDATED_BALANCE = 2F;

    @Inject
    private UserWalletRepository userWalletRepository;

    @Inject
    private UserWalletService userWalletService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserWalletMockMvc;

    private UserWallet userWallet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserWalletResource userWalletResource = new UserWalletResource();
        ReflectionTestUtils.setField(userWalletResource, "userWalletService", userWalletService);
        this.restUserWalletMockMvc = MockMvcBuilders.standaloneSetup(userWalletResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserWallet createEntity(EntityManager em) {
        UserWallet userWallet = new UserWallet()
                .balance(DEFAULT_BALANCE);
        return userWallet;
    }

    @Before
    public void initTest() {
        userWallet = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserWallet() throws Exception {
        int databaseSizeBeforeCreate = userWalletRepository.findAll().size();

        // Create the UserWallet

        restUserWalletMockMvc.perform(post("/api/user-wallets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userWallet)))
                .andExpect(status().isCreated());

        // Validate the UserWallet in the database
        List<UserWallet> userWallets = userWalletRepository.findAll();
        assertThat(userWallets).hasSize(databaseSizeBeforeCreate + 1);
        UserWallet testUserWallet = userWallets.get(userWallets.size() - 1);
        assertThat(testUserWallet.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllUserWallets() throws Exception {
        // Initialize the database
        userWalletRepository.saveAndFlush(userWallet);

        // Get all the userWallets
        restUserWalletMockMvc.perform(get("/api/user-wallets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userWallet.getId().intValue())))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void getUserWallet() throws Exception {
        // Initialize the database
        userWalletRepository.saveAndFlush(userWallet);

        // Get the userWallet
        restUserWalletMockMvc.perform(get("/api/user-wallets/{id}", userWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userWallet.getId().intValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserWallet() throws Exception {
        // Get the userWallet
        restUserWalletMockMvc.perform(get("/api/user-wallets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserWallet() throws Exception {
        // Initialize the database
        userWalletService.save(userWallet);

        int databaseSizeBeforeUpdate = userWalletRepository.findAll().size();

        // Update the userWallet
        UserWallet updatedUserWallet = userWalletRepository.findOne(userWallet.getId());
        updatedUserWallet
                .balance(UPDATED_BALANCE);

        restUserWalletMockMvc.perform(put("/api/user-wallets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserWallet)))
                .andExpect(status().isOk());

        // Validate the UserWallet in the database
        List<UserWallet> userWallets = userWalletRepository.findAll();
        assertThat(userWallets).hasSize(databaseSizeBeforeUpdate);
        UserWallet testUserWallet = userWallets.get(userWallets.size() - 1);
        assertThat(testUserWallet.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void deleteUserWallet() throws Exception {
        // Initialize the database
        userWalletService.save(userWallet);

        int databaseSizeBeforeDelete = userWalletRepository.findAll().size();

        // Get the userWallet
        restUserWalletMockMvc.perform(delete("/api/user-wallets/{id}", userWallet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserWallet> userWallets = userWalletRepository.findAll();
        assertThat(userWallets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
