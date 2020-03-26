package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.repository.ArticleTypeRepository;
import pt.tech4covid.service.ArticleTypeService;

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

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private ArticleTypeRepository articleTypeRepository;

    @Autowired
    private ArticleTypeService articleTypeService;

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
            .name(DEFAULT_NAME)
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
            .name(UPDATED_NAME)
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
        assertThat(testArticleType.getName()).isEqualTo(DEFAULT_NAME);
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
    public void getAllArticleTypes() throws Exception {
        // Initialize the database
        articleTypeRepository.saveAndFlush(articleType);

        // Get all the articleTypeList
        restArticleTypeMockMvc.perform(get("/api/article-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(articleType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
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
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
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
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);

        restArticleTypeMockMvc.perform(put("/api/article-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticleType)))
            .andExpect(status().isOk());

        // Validate the ArticleType in the database
        List<ArticleType> articleTypeList = articleTypeRepository.findAll();
        assertThat(articleTypeList).hasSize(databaseSizeBeforeUpdate);
        ArticleType testArticleType = articleTypeList.get(articleTypeList.size() - 1);
        assertThat(testArticleType.getName()).isEqualTo(UPDATED_NAME);
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
