package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.repository.CategoryTreeRepository;
import pt.tech4covid.service.CategoryTreeService;

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
 * Integration tests for the {@link CategoryTreeResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CategoryTreeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CategoryTreeRepository categoryTreeRepository;

    @Autowired
    private CategoryTreeService categoryTreeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryTreeMockMvc;

    private CategoryTree categoryTree;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryTree createEntity(EntityManager em) {
        CategoryTree categoryTree = new CategoryTree()
            .name(DEFAULT_NAME)
            .active(DEFAULT_ACTIVE);
        return categoryTree;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryTree createUpdatedEntity(EntityManager em) {
        CategoryTree categoryTree = new CategoryTree()
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);
        return categoryTree;
    }

    @BeforeEach
    public void initTest() {
        categoryTree = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategoryTree() throws Exception {
        int databaseSizeBeforeCreate = categoryTreeRepository.findAll().size();

        // Create the CategoryTree
        restCategoryTreeMockMvc.perform(post("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryTree)))
            .andExpect(status().isCreated());

        // Validate the CategoryTree in the database
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryTree testCategoryTree = categoryTreeList.get(categoryTreeList.size() - 1);
        assertThat(testCategoryTree.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategoryTree.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createCategoryTreeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryTreeRepository.findAll().size();

        // Create the CategoryTree with an existing ID
        categoryTree.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryTreeMockMvc.perform(post("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryTree)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryTree in the database
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCategoryTrees() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList
        restCategoryTreeMockMvc.perform(get("/api/category-trees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryTree.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCategoryTree() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get the categoryTree
        restCategoryTreeMockMvc.perform(get("/api/category-trees/{id}", categoryTree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryTree.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCategoryTree() throws Exception {
        // Get the categoryTree
        restCategoryTreeMockMvc.perform(get("/api/category-trees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoryTree() throws Exception {
        // Initialize the database
        categoryTreeService.save(categoryTree);

        int databaseSizeBeforeUpdate = categoryTreeRepository.findAll().size();

        // Update the categoryTree
        CategoryTree updatedCategoryTree = categoryTreeRepository.findById(categoryTree.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryTree are not directly saved in db
        em.detach(updatedCategoryTree);
        updatedCategoryTree
            .name(UPDATED_NAME)
            .active(UPDATED_ACTIVE);

        restCategoryTreeMockMvc.perform(put("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategoryTree)))
            .andExpect(status().isOk());

        // Validate the CategoryTree in the database
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeUpdate);
        CategoryTree testCategoryTree = categoryTreeList.get(categoryTreeList.size() - 1);
        assertThat(testCategoryTree.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategoryTree.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCategoryTree() throws Exception {
        int databaseSizeBeforeUpdate = categoryTreeRepository.findAll().size();

        // Create the CategoryTree

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryTreeMockMvc.perform(put("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryTree)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryTree in the database
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategoryTree() throws Exception {
        // Initialize the database
        categoryTreeService.save(categoryTree);

        int databaseSizeBeforeDelete = categoryTreeRepository.findAll().size();

        // Delete the categoryTree
        restCategoryTreeMockMvc.perform(delete("/api/category-trees/{id}", categoryTree.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
