package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.SourceRepo;
import pt.tech4covid.repository.SourceRepoRepository;
import pt.tech4covid.service.SourceRepoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SourceRepoResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SourceRepoResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private SourceRepoRepository sourceRepoRepository;

    @Autowired
    private SourceRepoService sourceRepoService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceRepoMockMvc;

    private SourceRepo sourceRepo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceRepo createEntity(EntityManager em) {
        SourceRepo sourceRepo = new SourceRepo()
            .itemName(DEFAULT_ITEM_NAME)
            .active(DEFAULT_ACTIVE);
        return sourceRepo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceRepo createUpdatedEntity(EntityManager em) {
        SourceRepo sourceRepo = new SourceRepo()
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);
        return sourceRepo;
    }

    @BeforeEach
    public void initTest() {
        sourceRepo = createEntity(em);
    }

    @Test
    @Transactional
    public void createSourceRepo() throws Exception {
        int databaseSizeBeforeCreate = sourceRepoRepository.findAll().size();

        // Create the SourceRepo
        restSourceRepoMockMvc.perform(post("/api/source-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourceRepo)))
            .andExpect(status().isCreated());

        // Validate the SourceRepo in the database
        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeCreate + 1);
        SourceRepo testSourceRepo = sourceRepoList.get(sourceRepoList.size() - 1);
        assertThat(testSourceRepo.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testSourceRepo.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createSourceRepoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceRepoRepository.findAll().size();

        // Create the SourceRepo with an existing ID
        sourceRepo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceRepoMockMvc.perform(post("/api/source-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourceRepo)))
            .andExpect(status().isBadRequest());

        // Validate the SourceRepo in the database
        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRepoRepository.findAll().size();
        // set the field null
        sourceRepo.setItemName(null);

        // Create the SourceRepo, which fails.

        restSourceRepoMockMvc.perform(post("/api/source-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourceRepo)))
            .andExpect(status().isBadRequest());

        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSourceRepos() throws Exception {
        // Initialize the database
        sourceRepoRepository.saveAndFlush(sourceRepo);

        // Get all the sourceRepoList
        restSourceRepoMockMvc.perform(get("/api/source-repos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceRepo.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSourceRepo() throws Exception {
        // Initialize the database
        sourceRepoRepository.saveAndFlush(sourceRepo);

        // Get the sourceRepo
        restSourceRepoMockMvc.perform(get("/api/source-repos/{id}", sourceRepo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceRepo.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSourceRepo() throws Exception {
        // Get the sourceRepo
        restSourceRepoMockMvc.perform(get("/api/source-repos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSourceRepo() throws Exception {
        // Initialize the database
        sourceRepoService.save(sourceRepo);

        int databaseSizeBeforeUpdate = sourceRepoRepository.findAll().size();

        // Update the sourceRepo
        SourceRepo updatedSourceRepo = sourceRepoRepository.findById(sourceRepo.getId()).get();
        // Disconnect from session so that the updates on updatedSourceRepo are not directly saved in db
        em.detach(updatedSourceRepo);
        updatedSourceRepo
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);

        restSourceRepoMockMvc.perform(put("/api/source-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSourceRepo)))
            .andExpect(status().isOk());

        // Validate the SourceRepo in the database
        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeUpdate);
        SourceRepo testSourceRepo = sourceRepoList.get(sourceRepoList.size() - 1);
        assertThat(testSourceRepo.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testSourceRepo.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingSourceRepo() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepoRepository.findAll().size();

        // Create the SourceRepo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceRepoMockMvc.perform(put("/api/source-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sourceRepo)))
            .andExpect(status().isBadRequest());

        // Validate the SourceRepo in the database
        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSourceRepo() throws Exception {
        // Initialize the database
        sourceRepoService.save(sourceRepo);

        int databaseSizeBeforeDelete = sourceRepoRepository.findAll().size();

        // Delete the sourceRepo
        restSourceRepoMockMvc.perform(delete("/api/source-repos/{id}", sourceRepo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourceRepo> sourceRepoList = sourceRepoRepository.findAll();
        assertThat(sourceRepoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
