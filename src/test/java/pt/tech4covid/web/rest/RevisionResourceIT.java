package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.Article;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.repository.RevisionRepository;
import pt.tech4covid.service.RevisionService;
import pt.tech4covid.service.dto.RevisionCriteria;
import pt.tech4covid.service.RevisionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pt.tech4covid.domain.enumeration.ReviewState;
/**
 * Integration tests for the {@link RevisionResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RevisionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PEER_REVIEWED = false;
    private static final Boolean UPDATED_IS_PEER_REVIEWED = true;

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REVIEW_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REVIEW_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REVIEW_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REVIEW_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    private static final ReviewState DEFAULT_REVIEW_STATE = ReviewState.Hold;
    private static final ReviewState UPDATED_REVIEW_STATE = ReviewState.OnGoing;

    @Autowired
    private RevisionRepository revisionRepository;

    @Mock
    private RevisionRepository revisionRepositoryMock;

    @Mock
    private RevisionService revisionServiceMock;

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private RevisionQueryService revisionQueryService;

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
            .isPeerReviewed(DEFAULT_IS_PEER_REVIEWED)
            .country(DEFAULT_COUNTRY)
            .keywords(DEFAULT_KEYWORDS)
            .reviewDate(DEFAULT_REVIEW_DATE)
            .reviewNotes(DEFAULT_REVIEW_NOTES)
            .author(DEFAULT_AUTHOR)
            .reviewer(DEFAULT_REVIEWER)
            .reviewState(DEFAULT_REVIEW_STATE);
        // Add required entity
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            article = ArticleResourceIT.createEntity(em);
            em.persist(article);
            em.flush();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        revision.setArticle(article);
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
            .isPeerReviewed(UPDATED_IS_PEER_REVIEWED)
            .country(UPDATED_COUNTRY)
            .keywords(UPDATED_KEYWORDS)
            .reviewDate(UPDATED_REVIEW_DATE)
            .reviewNotes(UPDATED_REVIEW_NOTES)
            .author(UPDATED_AUTHOR)
            .reviewer(UPDATED_REVIEWER)
            .reviewState(UPDATED_REVIEW_STATE);
        // Add required entity
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            article = ArticleResourceIT.createUpdatedEntity(em);
            em.persist(article);
            em.flush();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        revision.setArticle(article);
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
        assertThat(testRevision.isIsPeerReviewed()).isEqualTo(DEFAULT_IS_PEER_REVIEWED);
        assertThat(testRevision.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testRevision.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testRevision.getReviewDate()).isEqualTo(DEFAULT_REVIEW_DATE);
        assertThat(testRevision.getReviewNotes()).isEqualTo(DEFAULT_REVIEW_NOTES);
        assertThat(testRevision.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testRevision.getReviewer()).isEqualTo(DEFAULT_REVIEWER);
        assertThat(testRevision.getReviewState()).isEqualTo(DEFAULT_REVIEW_STATE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testRevision.getId()).isEqualTo(testRevision.getArticle().getId());
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
    public void updateRevisionMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        revisionService.save(revision);
        int databaseSizeBeforeCreate = revisionRepository.findAll().size();

        // Add a new parent entity
        Article article = ArticleResourceIT.createUpdatedEntity(em);
        em.persist(article);
        em.flush();

        // Load the revision
        Revision updatedRevision = revisionRepository.findById(revision.getId()).get();
        // Disconnect from session so that the updates on updatedRevision are not directly saved in db
        em.detach(updatedRevision);

        // Update the Article with new association value
        updatedRevision.setArticle(article);

        // Update the entity
        restRevisionMockMvc.perform(put("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRevision)))
            .andExpect(status().isOk());

        // Validate the Revision in the database
        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeCreate);
        Revision testRevision = revisionList.get(revisionList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testRevision.getId()).isEqualTo(testRevision.getArticle().getId());
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setTitle(null);

        // Create the Revision, which fails.

        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsPeerReviewedIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setIsPeerReviewed(null);

        // Create the Revision, which fails.

        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setAuthor(null);

        // Create the Revision, which fails.

        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReviewStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setReviewState(null);

        // Create the Revision, which fails.

        restRevisionMockMvc.perform(post("/api/revisions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revision)))
            .andExpect(status().isBadRequest());

        List<Revision> revisionList = revisionRepository.findAll();
        assertThat(revisionList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].isPeerReviewed").value(hasItem(DEFAULT_IS_PEER_REVIEWED.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewNotes").value(hasItem(DEFAULT_REVIEW_NOTES.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllRevisionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(revisionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRevisionMockMvc.perform(get("/api/revisions?eagerload=true"))
            .andExpect(status().isOk());

        verify(revisionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRevisionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(revisionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRevisionMockMvc.perform(get("/api/revisions?eagerload=true"))
            .andExpect(status().isOk());

        verify(revisionServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY.toString()))
            .andExpect(jsonPath("$.isPeerReviewed").value(DEFAULT_IS_PEER_REVIEWED.booleanValue()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.reviewDate").value(DEFAULT_REVIEW_DATE.toString()))
            .andExpect(jsonPath("$.reviewNotes").value(DEFAULT_REVIEW_NOTES.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER))
            .andExpect(jsonPath("$.reviewState").value(DEFAULT_REVIEW_STATE.toString()));
    }


    @Test
    @Transactional
    public void getRevisionsByIdFiltering() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        Long id = revision.getId();

        defaultRevisionShouldBeFound("id.equals=" + id);
        defaultRevisionShouldNotBeFound("id.notEquals=" + id);

        defaultRevisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRevisionShouldNotBeFound("id.greaterThan=" + id);

        defaultRevisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRevisionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRevisionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title equals to DEFAULT_TITLE
        defaultRevisionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the revisionList where title equals to UPDATED_TITLE
        defaultRevisionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title not equals to DEFAULT_TITLE
        defaultRevisionShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the revisionList where title not equals to UPDATED_TITLE
        defaultRevisionShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultRevisionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the revisionList where title equals to UPDATED_TITLE
        defaultRevisionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title is not null
        defaultRevisionShouldBeFound("title.specified=true");

        // Get all the revisionList where title is null
        defaultRevisionShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllRevisionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title contains DEFAULT_TITLE
        defaultRevisionShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the revisionList where title contains UPDATED_TITLE
        defaultRevisionShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where title does not contain DEFAULT_TITLE
        defaultRevisionShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the revisionList where title does not contain UPDATED_TITLE
        defaultRevisionShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllRevisionsByIsPeerReviewedIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where isPeerReviewed equals to DEFAULT_IS_PEER_REVIEWED
        defaultRevisionShouldBeFound("isPeerReviewed.equals=" + DEFAULT_IS_PEER_REVIEWED);

        // Get all the revisionList where isPeerReviewed equals to UPDATED_IS_PEER_REVIEWED
        defaultRevisionShouldNotBeFound("isPeerReviewed.equals=" + UPDATED_IS_PEER_REVIEWED);
    }

    @Test
    @Transactional
    public void getAllRevisionsByIsPeerReviewedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where isPeerReviewed not equals to DEFAULT_IS_PEER_REVIEWED
        defaultRevisionShouldNotBeFound("isPeerReviewed.notEquals=" + DEFAULT_IS_PEER_REVIEWED);

        // Get all the revisionList where isPeerReviewed not equals to UPDATED_IS_PEER_REVIEWED
        defaultRevisionShouldBeFound("isPeerReviewed.notEquals=" + UPDATED_IS_PEER_REVIEWED);
    }

    @Test
    @Transactional
    public void getAllRevisionsByIsPeerReviewedIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where isPeerReviewed in DEFAULT_IS_PEER_REVIEWED or UPDATED_IS_PEER_REVIEWED
        defaultRevisionShouldBeFound("isPeerReviewed.in=" + DEFAULT_IS_PEER_REVIEWED + "," + UPDATED_IS_PEER_REVIEWED);

        // Get all the revisionList where isPeerReviewed equals to UPDATED_IS_PEER_REVIEWED
        defaultRevisionShouldNotBeFound("isPeerReviewed.in=" + UPDATED_IS_PEER_REVIEWED);
    }

    @Test
    @Transactional
    public void getAllRevisionsByIsPeerReviewedIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where isPeerReviewed is not null
        defaultRevisionShouldBeFound("isPeerReviewed.specified=true");

        // Get all the revisionList where isPeerReviewed is null
        defaultRevisionShouldNotBeFound("isPeerReviewed.specified=false");
    }

    @Test
    @Transactional
    public void getAllRevisionsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country equals to DEFAULT_COUNTRY
        defaultRevisionShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the revisionList where country equals to UPDATED_COUNTRY
        defaultRevisionShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country not equals to DEFAULT_COUNTRY
        defaultRevisionShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the revisionList where country not equals to UPDATED_COUNTRY
        defaultRevisionShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultRevisionShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the revisionList where country equals to UPDATED_COUNTRY
        defaultRevisionShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country is not null
        defaultRevisionShouldBeFound("country.specified=true");

        // Get all the revisionList where country is null
        defaultRevisionShouldNotBeFound("country.specified=false");
    }
                @Test
    @Transactional
    public void getAllRevisionsByCountryContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country contains DEFAULT_COUNTRY
        defaultRevisionShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the revisionList where country contains UPDATED_COUNTRY
        defaultRevisionShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where country does not contain DEFAULT_COUNTRY
        defaultRevisionShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the revisionList where country does not contain UPDATED_COUNTRY
        defaultRevisionShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }


    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate equals to DEFAULT_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.equals=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate equals to UPDATED_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.equals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate not equals to DEFAULT_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.notEquals=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate not equals to UPDATED_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.notEquals=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate in DEFAULT_REVIEW_DATE or UPDATED_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.in=" + DEFAULT_REVIEW_DATE + "," + UPDATED_REVIEW_DATE);

        // Get all the revisionList where reviewDate equals to UPDATED_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.in=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate is not null
        defaultRevisionShouldBeFound("reviewDate.specified=true");

        // Get all the revisionList where reviewDate is null
        defaultRevisionShouldNotBeFound("reviewDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate is greater than or equal to DEFAULT_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.greaterThanOrEqual=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate is greater than or equal to UPDATED_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.greaterThanOrEqual=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate is less than or equal to DEFAULT_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.lessThanOrEqual=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate is less than or equal to SMALLER_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.lessThanOrEqual=" + SMALLER_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsLessThanSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate is less than DEFAULT_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.lessThan=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate is less than UPDATED_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.lessThan=" + UPDATED_REVIEW_DATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewDate is greater than DEFAULT_REVIEW_DATE
        defaultRevisionShouldNotBeFound("reviewDate.greaterThan=" + DEFAULT_REVIEW_DATE);

        // Get all the revisionList where reviewDate is greater than SMALLER_REVIEW_DATE
        defaultRevisionShouldBeFound("reviewDate.greaterThan=" + SMALLER_REVIEW_DATE);
    }


    @Test
    @Transactional
    public void getAllRevisionsByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author equals to DEFAULT_AUTHOR
        defaultRevisionShouldBeFound("author.equals=" + DEFAULT_AUTHOR);

        // Get all the revisionList where author equals to UPDATED_AUTHOR
        defaultRevisionShouldNotBeFound("author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllRevisionsByAuthorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author not equals to DEFAULT_AUTHOR
        defaultRevisionShouldNotBeFound("author.notEquals=" + DEFAULT_AUTHOR);

        // Get all the revisionList where author not equals to UPDATED_AUTHOR
        defaultRevisionShouldBeFound("author.notEquals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllRevisionsByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author in DEFAULT_AUTHOR or UPDATED_AUTHOR
        defaultRevisionShouldBeFound("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR);

        // Get all the revisionList where author equals to UPDATED_AUTHOR
        defaultRevisionShouldNotBeFound("author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllRevisionsByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author is not null
        defaultRevisionShouldBeFound("author.specified=true");

        // Get all the revisionList where author is null
        defaultRevisionShouldNotBeFound("author.specified=false");
    }
                @Test
    @Transactional
    public void getAllRevisionsByAuthorContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author contains DEFAULT_AUTHOR
        defaultRevisionShouldBeFound("author.contains=" + DEFAULT_AUTHOR);

        // Get all the revisionList where author contains UPDATED_AUTHOR
        defaultRevisionShouldNotBeFound("author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    public void getAllRevisionsByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where author does not contain DEFAULT_AUTHOR
        defaultRevisionShouldNotBeFound("author.doesNotContain=" + DEFAULT_AUTHOR);

        // Get all the revisionList where author does not contain UPDATED_AUTHOR
        defaultRevisionShouldBeFound("author.doesNotContain=" + UPDATED_AUTHOR);
    }


    @Test
    @Transactional
    public void getAllRevisionsByReviewerIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer equals to DEFAULT_REVIEWER
        defaultRevisionShouldBeFound("reviewer.equals=" + DEFAULT_REVIEWER);

        // Get all the revisionList where reviewer equals to UPDATED_REVIEWER
        defaultRevisionShouldNotBeFound("reviewer.equals=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer not equals to DEFAULT_REVIEWER
        defaultRevisionShouldNotBeFound("reviewer.notEquals=" + DEFAULT_REVIEWER);

        // Get all the revisionList where reviewer not equals to UPDATED_REVIEWER
        defaultRevisionShouldBeFound("reviewer.notEquals=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewerIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer in DEFAULT_REVIEWER or UPDATED_REVIEWER
        defaultRevisionShouldBeFound("reviewer.in=" + DEFAULT_REVIEWER + "," + UPDATED_REVIEWER);

        // Get all the revisionList where reviewer equals to UPDATED_REVIEWER
        defaultRevisionShouldNotBeFound("reviewer.in=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewerIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer is not null
        defaultRevisionShouldBeFound("reviewer.specified=true");

        // Get all the revisionList where reviewer is null
        defaultRevisionShouldNotBeFound("reviewer.specified=false");
    }
                @Test
    @Transactional
    public void getAllRevisionsByReviewerContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer contains DEFAULT_REVIEWER
        defaultRevisionShouldBeFound("reviewer.contains=" + DEFAULT_REVIEWER);

        // Get all the revisionList where reviewer contains UPDATED_REVIEWER
        defaultRevisionShouldNotBeFound("reviewer.contains=" + UPDATED_REVIEWER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewerNotContainsSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewer does not contain DEFAULT_REVIEWER
        defaultRevisionShouldNotBeFound("reviewer.doesNotContain=" + DEFAULT_REVIEWER);

        // Get all the revisionList where reviewer does not contain UPDATED_REVIEWER
        defaultRevisionShouldBeFound("reviewer.doesNotContain=" + UPDATED_REVIEWER);
    }


    @Test
    @Transactional
    public void getAllRevisionsByReviewStateIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewState equals to DEFAULT_REVIEW_STATE
        defaultRevisionShouldBeFound("reviewState.equals=" + DEFAULT_REVIEW_STATE);

        // Get all the revisionList where reviewState equals to UPDATED_REVIEW_STATE
        defaultRevisionShouldNotBeFound("reviewState.equals=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewState not equals to DEFAULT_REVIEW_STATE
        defaultRevisionShouldNotBeFound("reviewState.notEquals=" + DEFAULT_REVIEW_STATE);

        // Get all the revisionList where reviewState not equals to UPDATED_REVIEW_STATE
        defaultRevisionShouldBeFound("reviewState.notEquals=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewStateIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewState in DEFAULT_REVIEW_STATE or UPDATED_REVIEW_STATE
        defaultRevisionShouldBeFound("reviewState.in=" + DEFAULT_REVIEW_STATE + "," + UPDATED_REVIEW_STATE);

        // Get all the revisionList where reviewState equals to UPDATED_REVIEW_STATE
        defaultRevisionShouldNotBeFound("reviewState.in=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewState is not null
        defaultRevisionShouldBeFound("reviewState.specified=true");

        // Get all the revisionList where reviewState is null
        defaultRevisionShouldNotBeFound("reviewState.specified=false");
    }

    @Test
    @Transactional
    public void getAllRevisionsByArticleIsEqualToSomething() throws Exception {
        // Get already existing entity
        Article article = revision.getArticle();
        revisionRepository.saveAndFlush(revision);
        Long articleId = article.getId();

        // Get all the revisionList where article equals to articleId
        defaultRevisionShouldBeFound("articleId.equals=" + articleId);

        // Get all the revisionList where article equals to articleId + 1
        defaultRevisionShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }


    @Test
    @Transactional
    public void getAllRevisionsByCtreeIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        CategoryTree ctree = CategoryTreeResourceIT.createEntity(em);
        em.persist(ctree);
        em.flush();
        revision.addCtree(ctree);
        revisionRepository.saveAndFlush(revision);
        Long ctreeId = ctree.getId();

        // Get all the revisionList where ctree equals to ctreeId
        defaultRevisionShouldBeFound("ctreeId.equals=" + ctreeId);

        // Get all the revisionList where ctree equals to ctreeId + 1
        defaultRevisionShouldNotBeFound("ctreeId.equals=" + (ctreeId + 1));
    }


    @Test
    @Transactional
    public void getAllRevisionsByAtypeIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        ArticleType atype = ArticleTypeResourceIT.createEntity(em);
        em.persist(atype);
        em.flush();
        revision.setAtype(atype);
        revisionRepository.saveAndFlush(revision);
        Long atypeId = atype.getId();

        // Get all the revisionList where atype equals to atypeId
        defaultRevisionShouldBeFound("atypeId.equals=" + atypeId);

        // Get all the revisionList where atype equals to atypeId + 1
        defaultRevisionShouldNotBeFound("atypeId.equals=" + (atypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRevisionShouldBeFound(String filter) throws Exception {
        restRevisionMockMvc.perform(get("/api/revisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revision.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY.toString())))
            .andExpect(jsonPath("$.[*].isPeerReviewed").value(hasItem(DEFAULT_IS_PEER_REVIEWED.booleanValue())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewDate").value(hasItem(DEFAULT_REVIEW_DATE.toString())))
            .andExpect(jsonPath("$.[*].reviewNotes").value(hasItem(DEFAULT_REVIEW_NOTES.toString())))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())));

        // Check, that the count call also returns 1
        restRevisionMockMvc.perform(get("/api/revisions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRevisionShouldNotBeFound(String filter) throws Exception {
        restRevisionMockMvc.perform(get("/api/revisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRevisionMockMvc.perform(get("/api/revisions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        revisionService.save(revision);

        int databaseSizeBeforeUpdate = revisionRepository.findAll().size();

        // Update the revision
        Revision updatedRevision = revisionRepository.findById(revision.getId()).get();
        // Disconnect from session so that the updates on updatedRevision are not directly saved in db
        em.detach(updatedRevision);
        updatedRevision
            .title(UPDATED_TITLE)
            .summary(UPDATED_SUMMARY)
            .isPeerReviewed(UPDATED_IS_PEER_REVIEWED)
            .country(UPDATED_COUNTRY)
            .keywords(UPDATED_KEYWORDS)
            .reviewDate(UPDATED_REVIEW_DATE)
            .reviewNotes(UPDATED_REVIEW_NOTES)
            .author(UPDATED_AUTHOR)
            .reviewer(UPDATED_REVIEWER)
            .reviewState(UPDATED_REVIEW_STATE);

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
        assertThat(testRevision.isIsPeerReviewed()).isEqualTo(UPDATED_IS_PEER_REVIEWED);
        assertThat(testRevision.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testRevision.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testRevision.getReviewDate()).isEqualTo(UPDATED_REVIEW_DATE);
        assertThat(testRevision.getReviewNotes()).isEqualTo(UPDATED_REVIEW_NOTES);
        assertThat(testRevision.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testRevision.getReviewer()).isEqualTo(UPDATED_REVIEWER);
        assertThat(testRevision.getReviewState()).isEqualTo(UPDATED_REVIEW_STATE);
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
        revisionService.save(revision);

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
