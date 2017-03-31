package tn.insat.bourse.web.rest;

import tn.insat.bourse.InsatBourseApp;

import tn.insat.bourse.domain.Valeur;
import tn.insat.bourse.repository.ValeurRepository;
import tn.insat.bourse.service.ValeurService;

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
 * Test class for the ValeurResource REST controller.
 *
 * @see ValeurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatBourseApp.class)
public class ValeurResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_QTY = 0;
    private static final Integer UPDATED_QTY = 1;

    private static final Float DEFAULT_PRICE = 0F;
    private static final Float UPDATED_PRICE = 1F;

    @Inject
    private ValeurRepository valeurRepository;

    @Inject
    private ValeurService valeurService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restValeurMockMvc;

    private Valeur valeur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ValeurResource valeurResource = new ValeurResource();
        ReflectionTestUtils.setField(valeurResource, "valeurService", valeurService);
        this.restValeurMockMvc = MockMvcBuilders.standaloneSetup(valeurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valeur createEntity(EntityManager em) {
        Valeur valeur = new Valeur()
                .name(DEFAULT_NAME)
                .qty(DEFAULT_QTY)
                .price(DEFAULT_PRICE);
        return valeur;
    }

    @Before
    public void initTest() {
        valeur = createEntity(em);
    }

    @Test
    @Transactional
    public void createValeur() throws Exception {
        int databaseSizeBeforeCreate = valeurRepository.findAll().size();

        // Create the Valeur

        restValeurMockMvc.perform(post("/api/valeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(valeur)))
                .andExpect(status().isCreated());

        // Validate the Valeur in the database
        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeCreate + 1);
        Valeur testValeur = valeurs.get(valeurs.size() - 1);
        assertThat(testValeur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testValeur.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testValeur.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = valeurRepository.findAll().size();
        // set the field null
        valeur.setName(null);

        // Create the Valeur, which fails.

        restValeurMockMvc.perform(post("/api/valeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(valeur)))
                .andExpect(status().isBadRequest());

        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = valeurRepository.findAll().size();
        // set the field null
        valeur.setQty(null);

        // Create the Valeur, which fails.

        restValeurMockMvc.perform(post("/api/valeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(valeur)))
                .andExpect(status().isBadRequest());

        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = valeurRepository.findAll().size();
        // set the field null
        valeur.setPrice(null);

        // Create the Valeur, which fails.

        restValeurMockMvc.perform(post("/api/valeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(valeur)))
                .andExpect(status().isBadRequest());

        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllValeurs() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        // Get all the valeurs
        restValeurMockMvc.perform(get("/api/valeurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(valeur.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getValeur() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        // Get the valeur
        restValeurMockMvc.perform(get("/api/valeurs/{id}", valeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(valeur.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingValeur() throws Exception {
        // Get the valeur
        restValeurMockMvc.perform(get("/api/valeurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValeur() throws Exception {
        // Initialize the database
        valeurService.save(valeur);

        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();

        // Update the valeur
        Valeur updatedValeur = valeurRepository.findOne(valeur.getId());
        updatedValeur
                .name(UPDATED_NAME)
                .qty(UPDATED_QTY)
                .price(UPDATED_PRICE);

        restValeurMockMvc.perform(put("/api/valeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedValeur)))
                .andExpect(status().isOk());

        // Validate the Valeur in the database
        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeUpdate);
        Valeur testValeur = valeurs.get(valeurs.size() - 1);
        assertThat(testValeur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testValeur.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testValeur.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteValeur() throws Exception {
        // Initialize the database
        valeurService.save(valeur);

        int databaseSizeBeforeDelete = valeurRepository.findAll().size();

        // Get the valeur
        restValeurMockMvc.perform(delete("/api/valeurs/{id}", valeur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Valeur> valeurs = valeurRepository.findAll();
        assertThat(valeurs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
