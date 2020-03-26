package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.PublicationSource;
import pt.tech4covid.domain.Article;
import pt.tech4covid.repository.PublicationSourceRepository;
import pt.tech4covid.service.PublicationSourceService;
import pt.tech4covid.service.dto.PublicationSourceCriteria;
import pt.tech4covid.service.PublicationSourceQueryService;

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
 * Integration tests for the {@link PublicationSourceResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PublicationSourceResourceIT {

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private PublicationSourceRepository publicationSourceRepository;

    @Autowired
    private PublicationSourceService publicationSourceService;

    @Autowired
    private PublicationSourceQueryService publicationSourceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublicationSourceMockMvc;

    private PublicationSource publicationSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublicationSource createEntity(EntityManager em) {
        PublicationSource publicationSource = new PublicationSource()
            .sourceName(DEFAULT_SOURCE_NAME)
            .active(DEFAULT_ACTIVE);
        return publicationSource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublicationSource createUpdatedEntity(EntityManager em) {
        PublicationSource publicationSource = new PublicationSource()
            .sourceName(UPDATED_SOURCE_NAME)
            .active(UPDATED_ACTIVE);
        return publicationSource;
    }

    @BeforeEach
    public void initTest() {
        publicationSource = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublicationSource() throws Exception {
        int databaseSizeBeforeCreate = publicationSourceRepository.findAll().size();

        // Create the PublicationSource
        restPublicationSourceMockMvc.perform(post("/api/publication-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publicationSource)))
            .andExpect(status().isCreated());

        // Validate the PublicationSource in the database
        List<PublicationSource> publicationSourceList = publicationSourceRepository.findAll();
        assertThat(publicationSourceList).hasSize(databaseSizeBeforeCreate + 1);
        PublicationSource testPublicationSource = publicationSourceList.get(publicationSourceList.size() - 1);
        assertThat(testPublicationSource.getSourceName()).isEqualTo(DEFAULT_SOURCE_NAME);
        assertThat(testPublicationSource.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createPublicationSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publicationSourceRepository.findAll().size();

        // Create the PublicationSource with an existing ID
        publicationSource.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicationSourceMockMvc.perform(post("/api/publication-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publicationSource)))
            .andExpect(status().isBadRequest());

        // Validate the PublicationSource in the database
        List<PublicationSource> publicationSourceList = publicationSourceRepository.findAll();
        assertThat(publicationSourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPublicationSources() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList
        restPublicationSourceMockMvc.perform(get("/api/publication-sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicationSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPublicationSource() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get the publicationSource
        restPublicationSourceMockMvc.perform(get("/api/publication-sources/{id}", publicationSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publicationSource.getId().intValue()))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getPublicationSourcesByIdFiltering() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        Long id = publicationSource.getId();

        defaultPublicationSourceShouldBeFound("id.equals=" + id);
        defaultPublicationSourceShouldNotBeFound("id.notEquals=" + id);

        defaultPublicationSourceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPublicationSourceShouldNotBeFound("id.greaterThan=" + id);

        defaultPublicationSourceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPublicationSourceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName equals to DEFAULT_SOURCE_NAME
        defaultPublicationSourceShouldBeFound("sourceName.equals=" + DEFAULT_SOURCE_NAME);

        // Get all the publicationSourceList where sourceName equals to UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldNotBeFound("sourceName.equals=" + UPDATED_SOURCE_NAME);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName not equals to DEFAULT_SOURCE_NAME
        defaultPublicationSourceShouldNotBeFound("sourceName.notEquals=" + DEFAULT_SOURCE_NAME);

        // Get all the publicationSourceList where sourceName not equals to UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldBeFound("sourceName.notEquals=" + UPDATED_SOURCE_NAME);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameIsInShouldWork() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName in DEFAULT_SOURCE_NAME or UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldBeFound("sourceName.in=" + DEFAULT_SOURCE_NAME + "," + UPDATED_SOURCE_NAME);

        // Get all the publicationSourceList where sourceName equals to UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldNotBeFound("sourceName.in=" + UPDATED_SOURCE_NAME);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName is not null
        defaultPublicationSourceShouldBeFound("sourceName.specified=true");

        // Get all the publicationSourceList where sourceName is null
        defaultPublicationSourceShouldNotBeFound("sourceName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameContainsSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName contains DEFAULT_SOURCE_NAME
        defaultPublicationSourceShouldBeFound("sourceName.contains=" + DEFAULT_SOURCE_NAME);

        // Get all the publicationSourceList where sourceName contains UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldNotBeFound("sourceName.contains=" + UPDATED_SOURCE_NAME);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesBySourceNameNotContainsSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where sourceName does not contain DEFAULT_SOURCE_NAME
        defaultPublicationSourceShouldNotBeFound("sourceName.doesNotContain=" + DEFAULT_SOURCE_NAME);

        // Get all the publicationSourceList where sourceName does not contain UPDATED_SOURCE_NAME
        defaultPublicationSourceShouldBeFound("sourceName.doesNotContain=" + UPDATED_SOURCE_NAME);
    }


    @Test
    @Transactional
    public void getAllPublicationSourcesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where active equals to DEFAULT_ACTIVE
        defaultPublicationSourceShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the publicationSourceList where active equals to UPDATED_ACTIVE
        defaultPublicationSourceShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where active not equals to DEFAULT_ACTIVE
        defaultPublicationSourceShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the publicationSourceList where active not equals to UPDATED_ACTIVE
        defaultPublicationSourceShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPublicationSourceShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the publicationSourceList where active equals to UPDATED_ACTIVE
        defaultPublicationSourceShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);

        // Get all the publicationSourceList where active is not null
        defaultPublicationSourceShouldBeFound("active.specified=true");

        // Get all the publicationSourceList where active is null
        defaultPublicationSourceShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllPublicationSourcesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        publicationSourceRepository.saveAndFlush(publicationSource);
        Article name = ArticleResourceIT.createEntity(em);
        em.persist(name);
        em.flush();
        publicationSource.addName(name);
        publicationSourceRepository.saveAndFlush(publicationSource);
        Long nameId = name.getId();

        // Get all the publicationSourceList where name equals to nameId
        defaultPublicationSourceShouldBeFound("nameId.equals=" + nameId);

        // Get all the publicationSourceList where name equals to nameId + 1
        defaultPublicationSourceShouldNotBeFound("nameId.equals=" + (nameId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPublicationSourceShouldBeFound(String filter) throws Exception {
        restPublicationSourceMockMvc.perform(get("/api/publication-sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicationSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restPublicationSourceMockMvc.perform(get("/api/publication-sources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPublicationSourceShouldNotBeFound(String filter) throws Exception {
        restPublicationSourceMockMvc.perform(get("/api/publication-sources?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublicationSourceMockMvc.perform(get("/api/publication-sources/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPublicationSource() throws Exception {
        // Get the publicationSource
        restPublicationSourceMockMvc.perform(get("/api/publication-sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublicationSource() throws Exception {
        // Initialize the database
        publicationSourceService.save(publicationSource);

        int databaseSizeBeforeUpdate = publicationSourceRepository.findAll().size();

        // Update the publicationSource
        PublicationSource updatedPublicationSource = publicationSourceRepository.findById(publicationSource.getId()).get();
        // Disconnect from session so that the updates on updatedPublicationSource are not directly saved in db
        em.detach(updatedPublicationSource);
        updatedPublicationSource
            .sourceName(UPDATED_SOURCE_NAME)
            .active(UPDATED_ACTIVE);

        restPublicationSourceMockMvc.perform(put("/api/publication-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPublicationSource)))
            .andExpect(status().isOk());

        // Validate the PublicationSource in the database
        List<PublicationSource> publicationSourceList = publicationSourceRepository.findAll();
        assertThat(publicationSourceList).hasSize(databaseSizeBeforeUpdate);
        PublicationSource testPublicationSource = publicationSourceList.get(publicationSourceList.size() - 1);
        assertThat(testPublicationSource.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testPublicationSource.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingPublicationSource() throws Exception {
        int databaseSizeBeforeUpdate = publicationSourceRepository.findAll().size();

        // Create the PublicationSource

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublicationSourceMockMvc.perform(put("/api/publication-sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publicationSource)))
            .andExpect(status().isBadRequest());

        // Validate the PublicationSource in the database
        List<PublicationSource> publicationSourceList = publicationSourceRepository.findAll();
        assertThat(publicationSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePublicationSource() throws Exception {
        // Initialize the database
        publicationSourceService.save(publicationSource);

        int databaseSizeBeforeDelete = publicationSourceRepository.findAll().size();

        // Delete the publicationSource
        restPublicationSourceMockMvc.perform(delete("/api/publication-sources/{id}", publicationSource.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PublicationSource> publicationSourceList = publicationSourceRepository.findAll();
        assertThat(publicationSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
