package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.repository.ArticleTypeRepository;
import pt.tech4covid.service.ArticleTypeService;
import pt.tech4covid.service.dto.ArticleTypeCriteria;
import pt.tech4covid.service.ArticleTypeQueryService;

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
 * Integration tests for the {@link ArticleTypeResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ArticleTypeResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    @Autowired
    private ArticleTypeService articleTypeService;

    @Autowired
    private ArticleTypeQueryService articleTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleTypeMockMvc;

    private ArticleType articleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleType createEntity(EntityManager em) {
        ArticleType articleType = new ArticleType()
            .itemName(DEFAULT_ITEM_NAME)
            .active(DEFAULT_ACTIVE);
        return articleType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArticleType createUpdatedEntity(EntityManager em) {
        ArticleType articleType = new ArticleType()
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);
        return articleType;
    }

    @BeforeEach
    public void initTest() {
        articleType = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticleType() throws Exception {
        int databaseSizeBeforeCreate = articleTypeRepository.findAll().size();

        // Create the ArticleType
        restArticleTypeMockMvc.perform(post("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleType)))
            .andExpect(status().isCreated());

        // Validate the ArticleType in the database
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ArticleType testArticleType = articleTypeList.get(articleTypeList.size() - 1);
        assertThat(testArticleType.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testArticleType.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createArticleTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleTypeRepository.findAll().size();

        // Create the ArticleType with an existing ID
        articleType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleTypeMockMvc.perform(post("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleType)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleType in the database
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleTypeRepository.findAll().size();
        // set the field null
        articleType.setItemName(null);

        // Create the ArticleType, which fails.

        restArticleTypeMockMvc.perform(post("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleType)))
            .andExpect(status().isBadRequest());

        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticleTypes() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList
        restArticleTypeMockMvc.perform(get("/api/article-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getArticleType() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get the articleType
        restArticleTypeMockMvc.perform(get("/api/article-types/{id}", articleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(articleType.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getArticleTypesByIdFiltering() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        Long id = articleType.getId();

        defaultArticleTypeShouldBeFound("id.equals=" + id);
        defaultArticleTypeShouldNotBeFound("id.notEquals=" + id);

        defaultArticleTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArticleTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultArticleTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArticleTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArticleTypesByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName equals to DEFAULT_ITEM_NAME
        defaultArticleTypeShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the articleTypeList where itemName equals to UPDATED_ITEM_NAME
        defaultArticleTypeShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByItemNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName not equals to DEFAULT_ITEM_NAME
        defaultArticleTypeShouldNotBeFound("itemName.notEquals=" + DEFAULT_ITEM_NAME);

        // Get all the articleTypeList where itemName not equals to UPDATED_ITEM_NAME
        defaultArticleTypeShouldBeFound("itemName.notEquals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultArticleTypeShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the articleTypeList where itemName equals to UPDATED_ITEM_NAME
        defaultArticleTypeShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName is not null
        defaultArticleTypeShouldBeFound("itemName.specified=true");

        // Get all the articleTypeList where itemName is null
        defaultArticleTypeShouldNotBeFound("itemName.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticleTypesByItemNameContainsSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName contains DEFAULT_ITEM_NAME
        defaultArticleTypeShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the articleTypeList where itemName contains UPDATED_ITEM_NAME
        defaultArticleTypeShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where itemName does not contain DEFAULT_ITEM_NAME
        defaultArticleTypeShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the articleTypeList where itemName does not contain UPDATED_ITEM_NAME
        defaultArticleTypeShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }


    @Test
    @Transactional
    public void getAllArticleTypesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where active equals to DEFAULT_ACTIVE
        defaultArticleTypeShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the articleTypeList where active equals to UPDATED_ACTIVE
        defaultArticleTypeShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where active not equals to DEFAULT_ACTIVE
        defaultArticleTypeShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the articleTypeList where active not equals to UPDATED_ACTIVE
        defaultArticleTypeShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultArticleTypeShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the articleTypeList where active equals to UPDATED_ACTIVE
        defaultArticleTypeShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllArticleTypesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList where active is not null
        defaultArticleTypeShouldBeFound("active.specified=true");

        // Get all the articleTypeList where active is null
        defaultArticleTypeShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticleTypesByRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);
        Revision revision = RevisionResourceIT.createEntity(em);
        em.persist(revision);
        em.flush();
        articleType.addRevision(revision);
        articleTypeRepository.saveAndFlush(articleType);
        Long revisionId = revision.getId();

        // Get all the articleTypeList where revision equals to revisionId
        defaultArticleTypeShouldBeFound("revisionId.equals=" + revisionId);

        // Get all the articleTypeList where revision equals to revisionId + 1
        defaultArticleTypeShouldNotBeFound("revisionId.equals=" + (revisionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleTypeShouldBeFound(String filter) throws Exception {
        restArticleTypeMockMvc.perform(get("/api/article-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restArticleTypeMockMvc.perform(get("/api/article-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleTypeShouldNotBeFound(String filter) throws Exception {
        restArticleTypeMockMvc.perform(get("/api/article-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleTypeMockMvc.perform(get("/api/article-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingArticleType() throws Exception {
        // Get the articleType
        restArticleTypeMockMvc.perform(get("/api/article-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticleType() throws Exception {
        // Initialize the database
        articleTypeService.save(articleType);

        int databaseSizeBeforeUpdate = articleTypeRepository.findAll().size();

        // Update the articleType
        ArticleType updatedArticleType = articleTypeRepository.findById(articleType.getId()).get();
        // Disconnect from session so that the updates on updatedArticleType are not directly saved in db
        em.detach(updatedArticleType);
        updatedArticleType
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);

        restArticleTypeMockMvc.perform(put("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticleType)))
            .andExpect(status().isOk());

        // Validate the ArticleType in the database
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeUpdate);
        ArticleType testArticleType = articleTypeList.get(articleTypeList.size() - 1);
        assertThat(testArticleType.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testArticleType.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingArticleType() throws Exception {
        int databaseSizeBeforeUpdate = articleTypeRepository.findAll().size();

        // Create the ArticleType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleTypeMockMvc.perform(put("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(articleType)))
            .andExpect(status().isBadRequest());

        // Validate the ArticleType in the database
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArticleType() throws Exception {
        // Initialize the database
        articleTypeService.save(articleType);

        int databaseSizeBeforeDelete = articleTypeRepository.findAll().size();

        // Delete the articleType
        restArticleTypeMockMvc.perform(delete("/api/article-types/{id}", articleType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
