package tn.insat.bourse.web.rest;

import tn.insat.bourse.InsatBourseApp;

import tn.insat.bourse.domain.Portefeuil;
import tn.insat.bourse.repository.PortefeuilRepository;

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
 * Test class for the PortefeuilResource REST controller.
 *
 * @see PortefeuilResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatBourseApp.class)
public class PortefeuilResourceIntTest {

    private static final Float DEFAULT_SOLDE = 1F;
    private static final Float UPDATED_SOLDE = 2F;

    @Inject
    private PortefeuilRepository portefeuilRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPortefeuilMockMvc;

    private Portefeuil portefeuil;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PortefeuilResource portefeuilResource = new PortefeuilResource();
        ReflectionTestUtils.setField(portefeuilResource, "portefeuilRepository", portefeuilRepository);
        this.restPortefeuilMockMvc = MockMvcBuilders.standaloneSetup(portefeuilResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Portefeuil createEntity(EntityManager em) {
        Portefeuil portefeuil = new Portefeuil()
                .solde(DEFAULT_SOLDE);
        return portefeuil;
    }

    @Before
    public void initTest() {
        portefeuil = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortefeuil() throws Exception {
        int databaseSizeBeforeCreate = portefeuilRepository.findAll().size();

        // Create the Portefeuil

        restPortefeuilMockMvc.perform(post("/api/portefeuils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(portefeuil)))
                .andExpect(status().isCreated());

        // Validate the Portefeuil in the database
        List<Portefeuil> portefeuils = portefeuilRepository.findAll();
        assertThat(portefeuils).hasSize(databaseSizeBeforeCreate + 1);
        Portefeuil testPortefeuil = portefeuils.get(portefeuils.size() - 1);
        assertThat(testPortefeuil.getSolde()).isEqualTo(DEFAULT_SOLDE);
    }

    @Test
    @Transactional
    public void getAllPortefeuils() throws Exception {
        // Initialize the database
        portefeuilRepository.saveAndFlush(portefeuil);

        // Get all the portefeuils
        restPortefeuilMockMvc.perform(get("/api/portefeuils?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(portefeuil.getId().intValue())))
                .andExpect(jsonPath("$.[*].solde").value(hasItem(DEFAULT_SOLDE.doubleValue())));
    }

    @Test
    @Transactional
    public void getPortefeuil() throws Exception {
        // Initialize the database
        portefeuilRepository.saveAndFlush(portefeuil);

        // Get the portefeuil
        restPortefeuilMockMvc.perform(get("/api/portefeuils/{id}", portefeuil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(portefeuil.getId().intValue()))
            .andExpect(jsonPath("$.solde").value(DEFAULT_SOLDE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPortefeuil() throws Exception {
        // Get the portefeuil
        restPortefeuilMockMvc.perform(get("/api/portefeuils/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortefeuil() throws Exception {
        // Initialize the database
        portefeuilRepository.saveAndFlush(portefeuil);
        int databaseSizeBeforeUpdate = portefeuilRepository.findAll().size();

        // Update the portefeuil
        Portefeuil updatedPortefeuil = portefeuilRepository.findOne(portefeuil.getId());
        updatedPortefeuil
                .solde(UPDATED_SOLDE);

        restPortefeuilMockMvc.perform(put("/api/portefeuils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPortefeuil)))
                .andExpect(status().isOk());

        // Validate the Portefeuil in the database
        List<Portefeuil> portefeuils = portefeuilRepository.findAll();
        assertThat(portefeuils).hasSize(databaseSizeBeforeUpdate);
        Portefeuil testPortefeuil = portefeuils.get(portefeuils.size() - 1);
        assertThat(testPortefeuil.getSolde()).isEqualTo(UPDATED_SOLDE);
    }

    @Test
    @Transactional
    public void deletePortefeuil() throws Exception {
        // Initialize the database
        portefeuilRepository.saveAndFlush(portefeuil);
        int databaseSizeBeforeDelete = portefeuilRepository.findAll().size();

        // Get the portefeuil
        restPortefeuilMockMvc.perform(delete("/api/portefeuils/{id}", portefeuil.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Portefeuil> portefeuils = portefeuilRepository.findAll();
        assertThat(portefeuils).hasSize(databaseSizeBeforeDelete - 1);
    }
}
