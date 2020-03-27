package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.ContentSource;
import pt.tech4covid.repository.ContentSourceRepository;
import pt.tech4covid.service.ContentSourceService;

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
 * Integration tests for the {@link ContentSourceResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ContentSourceResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ContentSourceRepository contentSourceRepository;

    @Autowired
    private ContentSourceService contentSourceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContentSourceMockMvc;

    private ContentSource contentSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentSource createEntity(EntityManager em) {
        ContentSource contentSource = new ContentSource()
            .itemName(DEFAULT_ITEM_NAME)
            .active(DEFAULT_ACTIVE);
        return contentSource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentSource createUpdatedEntity(EntityManager em) {
        ContentSource contentSource = new ContentSource()
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);
        return contentSource;
    }

    @BeforeEach
    public void initTest() {
        contentSource = createEntity(em);
    }

    @Test
    @Transactional
    public void createContentSource() throws Exception {
        int databaseSizeBeforeCreate = contentSourceRepository.findAll().size();

        // Create the ContentSource
        restContentSourceMockMvc.perform(post("/api/content-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentSource)))
            .andExpect(status().isCreated());

        // Validate the ContentSource in the database
        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeCreate + 1);
        ContentSource testContentSource = contentSourceList.get(contentSourceList.size() - 1);
        assertThat(testContentSource.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testContentSource.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createContentSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentSourceRepository.findAll().size();

        // Create the ContentSource with an existing ID
        contentSource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentSourceMockMvc.perform(post("/api/content-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentSource)))
            .andExpect(status().isBadRequest());

        // Validate the ContentSource in the database
        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentSourceRepository.findAll().size();
        // set the field null
        contentSource.setItemName(null);

        // Create the ContentSource, which fails.

        restContentSourceMockMvc.perform(post("/api/content-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentSource)))
            .andExpect(status().isBadRequest());

        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContentSources() throws Exception {
        // Initialize the database
        contentSourceRepository.saveAndFlush(contentSource);

        // Get all the contentSourceList
        restContentSourceMockMvc.perform(get("/api/content-sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getContentSource() throws Exception {
        // Initialize the database
        contentSourceRepository.saveAndFlush(contentSource);

        // Get the contentSource
        restContentSourceMockMvc.perform(get("/api/content-sources/{id}", contentSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contentSource.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingContentSource() throws Exception {
        // Get the contentSource
        restContentSourceMockMvc.perform(get("/api/content-sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContentSource() throws Exception {
        // Initialize the database
        contentSourceService.save(contentSource);

        int databaseSizeBeforeUpdate = contentSourceRepository.findAll().size();

        // Update the contentSource
        ContentSource updatedContentSource = contentSourceRepository.findById(contentSource.getId()).get();
        // Disconnect from session so that the updates on updatedContentSource are not directly saved in db
        em.detach(updatedContentSource);
        updatedContentSource
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);

        restContentSourceMockMvc.perform(put("/api/content-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedContentSource)))
            .andExpect(status().isOk());

        // Validate the ContentSource in the database
        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeUpdate);
        ContentSource testContentSource = contentSourceList.get(contentSourceList.size() - 1);
        assertThat(testContentSource.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testContentSource.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingContentSource() throws Exception {
        int databaseSizeBeforeUpdate = contentSourceRepository.findAll().size();

        // Create the ContentSource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContentSourceMockMvc.perform(put("/api/content-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(contentSource)))
            .andExpect(status().isBadRequest());

        // Validate the ContentSource in the database
        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteContentSource() throws Exception {
        // Initialize the database
        contentSourceService.save(contentSource);

        int databaseSizeBeforeDelete = contentSourceRepository.findAll().size();

        // Delete the contentSource
        restContentSourceMockMvc.perform(delete("/api/content-sources/{id}", contentSource.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContentSource> contentSourceList = contentSourceRepository.findAll();
        assertThat(contentSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
