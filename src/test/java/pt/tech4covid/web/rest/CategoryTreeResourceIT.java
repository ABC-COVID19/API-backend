package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.repository.CategoryTreeRepository;
import pt.tech4covid.service.CategoryTreeService;
import pt.tech4covid.service.dto.CategoryTreeCriteria;
import pt.tech4covid.service.CategoryTreeQueryService;

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

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CategoryTreeRepository categoryTreeRepository;

    @Autowired
    private CategoryTreeService categoryTreeService;

    @Autowired
    private CategoryTreeQueryService categoryTreeQueryService;

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
            .itemName(DEFAULT_ITEM_NAME)
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
            .itemName(UPDATED_ITEM_NAME)
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
        assertThat(testCategoryTree.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
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
    public void checkItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryTreeRepository.findAll().size();
        // set the field null
        categoryTree.setItemName(null);

        // Create the CategoryTree, which fails.

        restCategoryTreeMockMvc.perform(post("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(categoryTree)))
            .andExpect(status().isBadRequest());

        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
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
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }


    @Test
    @Transactional
    public void getCategoryTreesByIdFiltering() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        Long id = categoryTree.getId();

        defaultCategoryTreeShouldBeFound("id.equals=" + id);
        defaultCategoryTreeShouldNotBeFound("id.notEquals=" + id);

        defaultCategoryTreeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoryTreeShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoryTreeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoryTreeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCategoryTreesByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName equals to DEFAULT_ITEM_NAME
        defaultCategoryTreeShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the categoryTreeList where itemName equals to UPDATED_ITEM_NAME
        defaultCategoryTreeShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByItemNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName not equals to DEFAULT_ITEM_NAME
        defaultCategoryTreeShouldNotBeFound("itemName.notEquals=" + DEFAULT_ITEM_NAME);

        // Get all the categoryTreeList where itemName not equals to UPDATED_ITEM_NAME
        defaultCategoryTreeShouldBeFound("itemName.notEquals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultCategoryTreeShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the categoryTreeList where itemName equals to UPDATED_ITEM_NAME
        defaultCategoryTreeShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName is not null
        defaultCategoryTreeShouldBeFound("itemName.specified=true");

        // Get all the categoryTreeList where itemName is null
        defaultCategoryTreeShouldNotBeFound("itemName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoryTreesByItemNameContainsSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName contains DEFAULT_ITEM_NAME
        defaultCategoryTreeShouldBeFound("itemName.contains=" + DEFAULT_ITEM_NAME);

        // Get all the categoryTreeList where itemName contains UPDATED_ITEM_NAME
        defaultCategoryTreeShouldNotBeFound("itemName.contains=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where itemName does not contain DEFAULT_ITEM_NAME
        defaultCategoryTreeShouldNotBeFound("itemName.doesNotContain=" + DEFAULT_ITEM_NAME);

        // Get all the categoryTreeList where itemName does not contain UPDATED_ITEM_NAME
        defaultCategoryTreeShouldBeFound("itemName.doesNotContain=" + UPDATED_ITEM_NAME);
    }


    @Test
    @Transactional
    public void getAllCategoryTreesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where active equals to DEFAULT_ACTIVE
        defaultCategoryTreeShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the categoryTreeList where active equals to UPDATED_ACTIVE
        defaultCategoryTreeShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where active not equals to DEFAULT_ACTIVE
        defaultCategoryTreeShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the categoryTreeList where active not equals to UPDATED_ACTIVE
        defaultCategoryTreeShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultCategoryTreeShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the categoryTreeList where active equals to UPDATED_ACTIVE
        defaultCategoryTreeShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);

        // Get all the categoryTreeList where active is not null
        defaultCategoryTreeShouldBeFound("active.specified=true");

        // Get all the categoryTreeList where active is null
        defaultCategoryTreeShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoryTreesByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);
        CategoryTree children = CategoryTreeResourceIT.createEntity(em);
        em.persist(children);
        em.flush();
        categoryTree.addChildren(children);
        categoryTreeRepository.saveAndFlush(categoryTree);
        Long childrenId = children.getId();

        // Get all the categoryTreeList where children equals to childrenId
        defaultCategoryTreeShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the categoryTreeList where children equals to childrenId + 1
        defaultCategoryTreeShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }


    @Test
    @Transactional
    public void getAllCategoryTreesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);
        CategoryTree parent = CategoryTreeResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        categoryTree.setParent(parent);
        categoryTreeRepository.saveAndFlush(categoryTree);
        Long parentId = parent.getId();

        // Get all the categoryTreeList where parent equals to parentId
        defaultCategoryTreeShouldBeFound("parentId.equals=" + parentId);

        // Get all the categoryTreeList where parent equals to parentId + 1
        defaultCategoryTreeShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllCategoryTreesByNewsletterIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);
        Newsletter newsletter = NewsletterResourceIT.createEntity(em);
        em.persist(newsletter);
        em.flush();
        categoryTree.addNewsletter(newsletter);
        categoryTreeRepository.saveAndFlush(categoryTree);
        Long newsletterId = newsletter.getId();

        // Get all the categoryTreeList where newsletter equals to newsletterId
        defaultCategoryTreeShouldBeFound("newsletterId.equals=" + newsletterId);

        // Get all the categoryTreeList where newsletter equals to newsletterId + 1
        defaultCategoryTreeShouldNotBeFound("newsletterId.equals=" + (newsletterId + 1));
    }


    @Test
    @Transactional
    public void getAllCategoryTreesByRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryTreeRepository.saveAndFlush(categoryTree);
        Revision revision = RevisionResourceIT.createEntity(em);
        em.persist(revision);
        em.flush();
        categoryTree.addRevision(revision);
        categoryTreeRepository.saveAndFlush(categoryTree);
        Long revisionId = revision.getId();

        // Get all the categoryTreeList where revision equals to revisionId
        defaultCategoryTreeShouldBeFound("revisionId.equals=" + revisionId);

        // Get all the categoryTreeList where revision equals to revisionId + 1
        defaultCategoryTreeShouldNotBeFound("revisionId.equals=" + (revisionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryTreeShouldBeFound(String filter) throws Exception {
        restCategoryTreeMockMvc.perform(get("/api/category-trees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryTree.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restCategoryTreeMockMvc.perform(get("/api/category-trees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryTreeShouldNotBeFound(String filter) throws Exception {
        restCategoryTreeMockMvc.perform(get("/api/category-trees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryTreeMockMvc.perform(get("/api/category-trees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .itemName(UPDATED_ITEM_NAME)
            .active(UPDATED_ACTIVE);

        restCategoryTreeMockMvc.perform(put("/api/category-trees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategoryTree)))
            .andExpect(status().isOk());

        // Validate the CategoryTree in the database
        List<CategoryTree> categoryTreeList = categoryTreeRepository.findAll();
        assertThat(categoryTreeList).hasSize(databaseSizeBeforeUpdate);
        CategoryTree testCategoryTree = categoryTreeList.get(categoryTreeList.size() - 1);
        assertThat(testCategoryTree.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
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
