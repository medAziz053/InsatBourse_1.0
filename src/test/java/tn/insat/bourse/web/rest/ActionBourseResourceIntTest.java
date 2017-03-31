package tn.insat.bourse.web.rest;

import tn.insat.bourse.InsatBourseApp;

import tn.insat.bourse.domain.ActionBourse;
import tn.insat.bourse.repository.ActionBourseRepository;

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
 * Test class for the ActionBourseResource REST controller.
 *
 * @see ActionBourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InsatBourseApp.class)
public class ActionBourseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Inject
    private ActionBourseRepository actionBourseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restActionBourseMockMvc;

    private ActionBourse actionBourse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActionBourseResource actionBourseResource = new ActionBourseResource();
        ReflectionTestUtils.setField(actionBourseResource, "actionBourseRepository", actionBourseRepository);
        this.restActionBourseMockMvc = MockMvcBuilders.standaloneSetup(actionBourseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActionBourse createEntity(EntityManager em) {
        ActionBourse actionBourse = new ActionBourse()
                .name(DEFAULT_NAME)
                .price(DEFAULT_PRICE)
                .quantity(DEFAULT_QUANTITY);
        return actionBourse;
    }

    @Before
    public void initTest() {
        actionBourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createActionBourse() throws Exception {
        int databaseSizeBeforeCreate = actionBourseRepository.findAll().size();

        // Create the ActionBourse

        restActionBourseMockMvc.perform(post("/api/action-bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actionBourse)))
                .andExpect(status().isCreated());

        // Validate the ActionBourse in the database
        List<ActionBourse> actionBourses = actionBourseRepository.findAll();
        assertThat(actionBourses).hasSize(databaseSizeBeforeCreate + 1);
        ActionBourse testActionBourse = actionBourses.get(actionBourses.size() - 1);
        assertThat(testActionBourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActionBourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testActionBourse.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllActionBourses() throws Exception {
        // Initialize the database
        actionBourseRepository.saveAndFlush(actionBourse);

        // Get all the actionBourses
        restActionBourseMockMvc.perform(get("/api/action-bourses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actionBourse.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getActionBourse() throws Exception {
        // Initialize the database
        actionBourseRepository.saveAndFlush(actionBourse);

        // Get the actionBourse
        restActionBourseMockMvc.perform(get("/api/action-bourses/{id}", actionBourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actionBourse.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingActionBourse() throws Exception {
        // Get the actionBourse
        restActionBourseMockMvc.perform(get("/api/action-bourses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionBourse() throws Exception {
        // Initialize the database
        actionBourseRepository.saveAndFlush(actionBourse);
        int databaseSizeBeforeUpdate = actionBourseRepository.findAll().size();

        // Update the actionBourse
        ActionBourse updatedActionBourse = actionBourseRepository.findOne(actionBourse.getId());
        updatedActionBourse
                .name(UPDATED_NAME)
                .price(UPDATED_PRICE)
                .quantity(UPDATED_QUANTITY);

        restActionBourseMockMvc.perform(put("/api/action-bourses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActionBourse)))
                .andExpect(status().isOk());

        // Validate the ActionBourse in the database
        List<ActionBourse> actionBourses = actionBourseRepository.findAll();
        assertThat(actionBourses).hasSize(databaseSizeBeforeUpdate);
        ActionBourse testActionBourse = actionBourses.get(actionBourses.size() - 1);
        assertThat(testActionBourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActionBourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testActionBourse.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void deleteActionBourse() throws Exception {
        // Initialize the database
        actionBourseRepository.saveAndFlush(actionBourse);
        int databaseSizeBeforeDelete = actionBourseRepository.findAll().size();

        // Get the actionBourse
        restActionBourseMockMvc.perform(delete("/api/action-bourses/{id}", actionBourse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ActionBourse> actionBourses = actionBourseRepository.findAll();
        assertThat(actionBourses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
