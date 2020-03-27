package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.Article;
import pt.tech4covid.repository.RevisionRepository;
import pt.tech4covid.service.RevisionService;
import pt.tech4covid.service.dto.RevisionCriteria;
import pt.tech4covid.service.RevisionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pt.tech4covid.domain.enumeration.ReviewState;
/**
 * Integration tests for the {@link RevisionResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RevisionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_SUMMARY = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEWER = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final ReviewState DEFAULT_REVIEW_STATE = ReviewState.Hold;
    private static final ReviewState UPDATED_REVIEW_STATE = ReviewState.OnGoing;

    private static final String DEFAULT_RETURN_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_RETURN_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REVIEWED_BY_PEER = false;
    private static final Boolean UPDATED_REVIEWED_BY_PEER = true;

    private static final Integer DEFAULT_COMMUNITY_VOTES = 1;
    private static final Integer UPDATED_COMMUNITY_VOTES = 2;
    private static final Integer SMALLER_COMMUNITY_VOTES = 1 - 1;

    @Autowired
    private RevisionRepository revisionRepository;

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
            .reviewer(DEFAULT_REVIEWER)
            .active(DEFAULT_ACTIVE)
            .keywords(DEFAULT_KEYWORDS)
            .reviewState(DEFAULT_REVIEW_STATE)
            .returnNotes(DEFAULT_RETURN_NOTES)
            .reviewedByPeer(DEFAULT_REVIEWED_BY_PEER)
            .communityVotes(DEFAULT_COMMUNITY_VOTES);
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
            .reviewer(UPDATED_REVIEWER)
            .active(UPDATED_ACTIVE)
            .keywords(UPDATED_KEYWORDS)
            .reviewState(UPDATED_REVIEW_STATE)
            .returnNotes(UPDATED_RETURN_NOTES)
            .reviewedByPeer(UPDATED_REVIEWED_BY_PEER)
            .communityVotes(UPDATED_COMMUNITY_VOTES);
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
        assertThat(testRevision.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testRevision.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testRevision.getReviewState()).isEqualTo(DEFAULT_REVIEW_STATE);
        assertThat(testRevision.getReturnNotes()).isEqualTo(DEFAULT_RETURN_NOTES);
        assertThat(testRevision.isReviewedByPeer()).isEqualTo(DEFAULT_REVIEWED_BY_PEER);
        assertThat(testRevision.getCommunityVotes()).isEqualTo(DEFAULT_COMMUNITY_VOTES);
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
    public void checkReviewerIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setReviewer(null);

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
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setActive(null);

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
    public void checkReviewedByPeerIsRequired() throws Exception {
        int databaseSizeBeforeTest = revisionRepository.findAll().size();
        // set the field null
        revision.setReviewedByPeer(null);

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
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())))
            .andExpect(jsonPath("$.[*].returnNotes").value(hasItem(DEFAULT_RETURN_NOTES.toString())))
            .andExpect(jsonPath("$.[*].reviewedByPeer").value(hasItem(DEFAULT_REVIEWED_BY_PEER.booleanValue())))
            .andExpect(jsonPath("$.[*].communityVotes").value(hasItem(DEFAULT_COMMUNITY_VOTES)));
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
            .andExpect(jsonPath("$.reviewer").value(DEFAULT_REVIEWER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.reviewState").value(DEFAULT_REVIEW_STATE.toString()))
            .andExpect(jsonPath("$.returnNotes").value(DEFAULT_RETURN_NOTES.toString()))
            .andExpect(jsonPath("$.reviewedByPeer").value(DEFAULT_REVIEWED_BY_PEER.booleanValue()))
            .andExpect(jsonPath("$.communityVotes").value(DEFAULT_COMMUNITY_VOTES));
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
    public void getAllRevisionsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where active equals to DEFAULT_ACTIVE
        defaultRevisionShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the revisionList where active equals to UPDATED_ACTIVE
        defaultRevisionShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where active not equals to DEFAULT_ACTIVE
        defaultRevisionShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the revisionList where active not equals to UPDATED_ACTIVE
        defaultRevisionShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultRevisionShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the revisionList where active equals to UPDATED_ACTIVE
        defaultRevisionShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRevisionsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where active is not null
        defaultRevisionShouldBeFound("active.specified=true");

        // Get all the revisionList where active is null
        defaultRevisionShouldNotBeFound("active.specified=false");
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
    public void getAllRevisionsByReviewedByPeerIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewedByPeer equals to DEFAULT_REVIEWED_BY_PEER
        defaultRevisionShouldBeFound("reviewedByPeer.equals=" + DEFAULT_REVIEWED_BY_PEER);

        // Get all the revisionList where reviewedByPeer equals to UPDATED_REVIEWED_BY_PEER
        defaultRevisionShouldNotBeFound("reviewedByPeer.equals=" + UPDATED_REVIEWED_BY_PEER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewedByPeerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewedByPeer not equals to DEFAULT_REVIEWED_BY_PEER
        defaultRevisionShouldNotBeFound("reviewedByPeer.notEquals=" + DEFAULT_REVIEWED_BY_PEER);

        // Get all the revisionList where reviewedByPeer not equals to UPDATED_REVIEWED_BY_PEER
        defaultRevisionShouldBeFound("reviewedByPeer.notEquals=" + UPDATED_REVIEWED_BY_PEER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewedByPeerIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewedByPeer in DEFAULT_REVIEWED_BY_PEER or UPDATED_REVIEWED_BY_PEER
        defaultRevisionShouldBeFound("reviewedByPeer.in=" + DEFAULT_REVIEWED_BY_PEER + "," + UPDATED_REVIEWED_BY_PEER);

        // Get all the revisionList where reviewedByPeer equals to UPDATED_REVIEWED_BY_PEER
        defaultRevisionShouldNotBeFound("reviewedByPeer.in=" + UPDATED_REVIEWED_BY_PEER);
    }

    @Test
    @Transactional
    public void getAllRevisionsByReviewedByPeerIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where reviewedByPeer is not null
        defaultRevisionShouldBeFound("reviewedByPeer.specified=true");

        // Get all the revisionList where reviewedByPeer is null
        defaultRevisionShouldNotBeFound("reviewedByPeer.specified=false");
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes equals to DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.equals=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes equals to UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.equals=" + UPDATED_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes not equals to DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.notEquals=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes not equals to UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.notEquals=" + UPDATED_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsInShouldWork() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes in DEFAULT_COMMUNITY_VOTES or UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.in=" + DEFAULT_COMMUNITY_VOTES + "," + UPDATED_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes equals to UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.in=" + UPDATED_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes is not null
        defaultRevisionShouldBeFound("communityVotes.specified=true");

        // Get all the revisionList where communityVotes is null
        defaultRevisionShouldNotBeFound("communityVotes.specified=false");
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes is greater than or equal to DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.greaterThanOrEqual=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes is greater than or equal to UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.greaterThanOrEqual=" + UPDATED_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes is less than or equal to DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.lessThanOrEqual=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes is less than or equal to SMALLER_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.lessThanOrEqual=" + SMALLER_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsLessThanSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes is less than DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.lessThan=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes is less than UPDATED_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.lessThan=" + UPDATED_COMMUNITY_VOTES);
    }

    @Test
    @Transactional
    public void getAllRevisionsByCommunityVotesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);

        // Get all the revisionList where communityVotes is greater than DEFAULT_COMMUNITY_VOTES
        defaultRevisionShouldNotBeFound("communityVotes.greaterThan=" + DEFAULT_COMMUNITY_VOTES);

        // Get all the revisionList where communityVotes is greater than SMALLER_COMMUNITY_VOTES
        defaultRevisionShouldBeFound("communityVotes.greaterThan=" + SMALLER_COMMUNITY_VOTES);
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


    @Test
    @Transactional
    public void getAllRevisionsByCtreeIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        CategoryTree ctree = CategoryTreeResourceIT.createEntity(em);
        em.persist(ctree);
        em.flush();
        revision.setCtree(ctree);
        revisionRepository.saveAndFlush(revision);
        Long ctreeId = ctree.getId();

        // Get all the revisionList where ctree equals to ctreeId
        defaultRevisionShouldBeFound("ctreeId.equals=" + ctreeId);

        // Get all the revisionList where ctree equals to ctreeId + 1
        defaultRevisionShouldNotBeFound("ctreeId.equals=" + (ctreeId + 1));
    }


    @Test
    @Transactional
    public void getAllRevisionsByArticleIsEqualToSomething() throws Exception {
        // Initialize the database
        revisionRepository.saveAndFlush(revision);
        Article article = ArticleResourceIT.createEntity(em);
        em.persist(article);
        em.flush();
        revision.setArticle(article);
        revisionRepository.saveAndFlush(revision);
        Long articleId = article.getId();

        // Get all the revisionList where article equals to articleId
        defaultRevisionShouldBeFound("articleId.equals=" + articleId);

        // Get all the revisionList where article equals to articleId + 1
        defaultRevisionShouldNotBeFound("articleId.equals=" + (articleId + 1));
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
            .andExpect(jsonPath("$.[*].reviewer").value(hasItem(DEFAULT_REVIEWER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())))
            .andExpect(jsonPath("$.[*].returnNotes").value(hasItem(DEFAULT_RETURN_NOTES.toString())))
            .andExpect(jsonPath("$.[*].reviewedByPeer").value(hasItem(DEFAULT_REVIEWED_BY_PEER.booleanValue())))
            .andExpect(jsonPath("$.[*].communityVotes").value(hasItem(DEFAULT_COMMUNITY_VOTES)));

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
            .reviewer(UPDATED_REVIEWER)
            .active(UPDATED_ACTIVE)
            .keywords(UPDATED_KEYWORDS)
            .reviewState(UPDATED_REVIEW_STATE)
            .returnNotes(UPDATED_RETURN_NOTES)
            .reviewedByPeer(UPDATED_REVIEWED_BY_PEER)
            .communityVotes(UPDATED_COMMUNITY_VOTES);

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
        assertThat(testRevision.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testRevision.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testRevision.getReviewState()).isEqualTo(UPDATED_REVIEW_STATE);
        assertThat(testRevision.getReturnNotes()).isEqualTo(UPDATED_RETURN_NOTES);
        assertThat(testRevision.isReviewedByPeer()).isEqualTo(UPDATED_REVIEWED_BY_PEER);
        assertThat(testRevision.getCommunityVotes()).isEqualTo(UPDATED_COMMUNITY_VOTES);
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
