package pt.tech4covid.web.rest;

import pt.tech4covid.Abccovid19App;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.repository.RevisionRepository;

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
 * Integration tests for the {@link RevisionResource} REST controller.
 */
@SpringBootTest(classes = Abccovid19App.class)

@AutoConfigureMockMvc
@WithMockUser
public class RevisionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    @Autowired
    private RevisionRepository revisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRevisionMockMvc;

    private Revision revision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revision createEntity(EntityManager em) {
        Revision revision = new Revision()
            .title(DEFAULT_TITLE)
            .summary(DEFAULT_SUMMARY)
            .reviewer(DEFAULT_REVIEWER);
        return revision;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revision createUpdatedEntity(EntityManager em) {
        Revision revision = new Revision()
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .reviewer(UPDATED_REVIEWER);
        return revision;
    }

    @BeforeEach
    public void initTest() {
        revision = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevision() throws Exception {
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // Create the Revision
        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isCreated());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate + 1);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRevision.getSummary()).isEqualTo(DEFAULT_SUMMARY);
        assertThat(testRevision.getReviewer()).isEqualTo(DEFAULT_REVIEWER);
    }

    @Test
    @Transactional
    public void createRevisionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // Create the Revision with an existing ID
        revision.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRevisions() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList
        restRevisionMockMvc.perform(get("/api/revisions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revision.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)));
    }
    
    @Test
    @Transactional
    public void getRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get the revision
        restRevisionMockMvc.perform(get("/api/revisions/{id}", revision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(revision.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER));
    }

    @Test
    @Transactional
    public void getNonExistingRevision() throws Exception {
        // Get the revision
        restRevisionMockMvc.perform(get("/api/revisions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision
        Revision updatedRevision = revisionRepository.findById(revision.getId()).get();
        // Disconnect from session so that the updates on updatedRevision are not directly saved in db
        em.detach(updatedRevision);
        updatedRevision
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .reviewer(UPDATED_REVIEWER);

        restRevisionMockMvc.perform(put("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRevision)))
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);
        assertThat(testRevision.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRevision.getSummary()).isEqualTo(UPDATED_SUMMARY);
        assertThat(testRevision.getReviewer()).isEqualTo(UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void updateNonExistingRevision() throws Exception {
        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Create the Revision

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevisionMockMvc.perform(put("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRevision() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        int databaseSizeBeforeDelete = revisionRepository.findAll().size();

        // Delete the revision
        restRevisionMockMvc.perform(delete("/api/revisions/{id}", revision.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
