package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Article;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.ContentSource;
import pt.tech4covid.repository.ArticleRepository;
import pt.tech4covid.service.ArticleService;
import pt.tech4covid.service.dto.ArticleCriteria;
import pt.tech4covid.service.ArticleQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pt.tech4covid.domain.enumeration.ReviewState;
/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ArticleResourceIT {

    private static final Integer DEFAULT_SOURCE_ID = 1;
    private static final Integer UPDATED_SOURCE_ID = 2;
    private static final Integer SMALLER_SOURCE_ID = 1 - 1;

    private static final LocalDate DEFAULT_SOURCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SOURCE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_SOURCE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_SOURCE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ABSTRACT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ABSTRACT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBMED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBMED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PUBMED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_OFFICIAL_PUB_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OFFICIAL_PUB_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_OFFICIAL_PUB_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DOI = "AAAAAAAAAA";
    private static final String UPDATED_DOI = "BBBBBBBBBB";

    private static final String DEFAULT_JOURNAL = "AAAAAAAAAA";
    private static final String UPDATED_JOURNAL = "BBBBBBBBBB";

    private static final String DEFAULT_CITATION = "AAAAAAAAAA";
    private static final String UPDATED_CITATION = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final ReviewState DEFAULT_REVIEW_STATE = ReviewState.Hold;
    private static final ReviewState UPDATED_REVIEW_STATE = ReviewState.OnGoing;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleQueryService articleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity(EntityManager em) {
        Article article = new Article()
            .sourceID(DEFAULT_SOURCE_ID)
            .sourceDate(DEFAULT_SOURCE_DATE)
            .sourceTitle(DEFAULT_SOURCE_TITLE)
            .sourceAbstract(DEFAULT_SOURCE_ABSTRACT)
            .pubmedDate(DEFAULT_PUBMED_DATE)
            .officialPubDate(DEFAULT_OFFICIAL_PUB_DATE)
            .doi(DEFAULT_DOI)
            .journal(DEFAULT_JOURNAL)
            .citation(DEFAULT_CITATION)
            .keywords(DEFAULT_KEYWORDS)
            .reviewState(DEFAULT_REVIEW_STATE);
        return article;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity(EntityManager em) {
        Article article = new Article()
            .sourceID(UPDATED_SOURCE_ID)
            .sourceDate(UPDATED_SOURCE_DATE)
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceAbstract(UPDATED_SOURCE_ABSTRACT)
            .pubmedDate(UPDATED_PUBMED_DATE)
            .officialPubDate(UPDATED_OFFICIAL_PUB_DATE)
            .doi(UPDATED_DOI)
            .journal(UPDATED_JOURNAL)
            .citation(UPDATED_CITATION)
            .keywords(UPDATED_KEYWORDS)
            .reviewState(UPDATED_REVIEW_STATE);
        return article;
    }

    @BeforeEach
    public void initTest() {
        article = createEntity(em);
    }

    @Test
    @Transactional
    public void createArticle() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isCreated());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate + 1);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getSourceID()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testArticle.getSourceDate()).isEqualTo(DEFAULT_SOURCE_DATE);
        assertThat(testArticle.getSourceTitle()).isEqualTo(DEFAULT_SOURCE_TITLE);
        assertThat(testArticle.getSourceAbstract()).isEqualTo(DEFAULT_SOURCE_ABSTRACT);
        assertThat(testArticle.getPubmedDate()).isEqualTo(DEFAULT_PUBMED_DATE);
        assertThat(testArticle.getOfficialPubDate()).isEqualTo(DEFAULT_OFFICIAL_PUB_DATE);
        assertThat(testArticle.getDoi()).isEqualTo(DEFAULT_DOI);
        assertThat(testArticle.getJournal()).isEqualTo(DEFAULT_JOURNAL);
        assertThat(testArticle.getCitation()).isEqualTo(DEFAULT_CITATION);
        assertThat(testArticle.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testArticle.getReviewState()).isEqualTo(DEFAULT_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void createArticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = articleRepository.findAll().size();

        // Create the Article with an existing ID
        article.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSourceIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setSourceID(null);

        // Create the Article, which fails.

        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setSourceDate(null);

        // Create the Article, which fails.

        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReviewStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = articleRepository.findAll().size();
        // set the field null
        article.setReviewState(null);

        // Create the Article, which fails.

        restArticleMockMvc.perform(post("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceID").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].sourceDate").value(hasItem(DEFAULT_SOURCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceAbstract").value(hasItem(DEFAULT_SOURCE_ABSTRACT)))
            .andExpect(jsonPath("$.[*].pubmedDate").value(hasItem(DEFAULT_PUBMED_DATE.toString())))
            .andExpect(jsonPath("$.[*].officialPubDate").value(hasItem(DEFAULT_OFFICIAL_PUB_DATE.toString())))
            .andExpect(jsonPath("$.[*].doi").value(hasItem(DEFAULT_DOI)))
            .andExpect(jsonPath("$.[*].journal").value(hasItem(DEFAULT_JOURNAL)))
            .andExpect(jsonPath("$.[*].citation").value(hasItem(DEFAULT_CITATION)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getArticle() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.sourceID").value(DEFAULT_SOURCE_ID))
            .andExpect(jsonPath("$.sourceDate").value(DEFAULT_SOURCE_DATE.toString()))
            .andExpect(jsonPath("$.sourceTitle").value(DEFAULT_SOURCE_TITLE))
            .andExpect(jsonPath("$.sourceAbstract").value(DEFAULT_SOURCE_ABSTRACT))
            .andExpect(jsonPath("$.pubmedDate").value(DEFAULT_PUBMED_DATE.toString()))
            .andExpect(jsonPath("$.officialPubDate").value(DEFAULT_OFFICIAL_PUB_DATE.toString()))
            .andExpect(jsonPath("$.doi").value(DEFAULT_DOI))
            .andExpect(jsonPath("$.journal").value(DEFAULT_JOURNAL))
            .andExpect(jsonPath("$.citation").value(DEFAULT_CITATION))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.reviewState").value(DEFAULT_REVIEW_STATE.toString()));
    }


    @Test
    @Transactional
    public void getArticlesByIdFiltering() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        Long id = article.getId();

        defaultArticleShouldBeFound("id.equals=" + id);
        defaultArticleShouldNotBeFound("id.notEquals=" + id);

        defaultArticleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.greaterThan=" + id);

        defaultArticleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultArticleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID equals to DEFAULT_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.equals=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID equals to UPDATED_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.equals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID not equals to DEFAULT_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.notEquals=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID not equals to UPDATED_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.notEquals=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID in DEFAULT_SOURCE_ID or UPDATED_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.in=" + DEFAULT_SOURCE_ID + "," + UPDATED_SOURCE_ID);

        // Get all the articleList where sourceID equals to UPDATED_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.in=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID is not null
        defaultArticleShouldBeFound("sourceID.specified=true");

        // Get all the articleList where sourceID is null
        defaultArticleShouldNotBeFound("sourceID.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID is greater than or equal to DEFAULT_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.greaterThanOrEqual=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID is greater than or equal to UPDATED_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.greaterThanOrEqual=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID is less than or equal to DEFAULT_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.lessThanOrEqual=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID is less than or equal to SMALLER_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.lessThanOrEqual=" + SMALLER_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID is less than DEFAULT_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.lessThan=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID is less than UPDATED_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.lessThan=" + UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceIDIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceID is greater than DEFAULT_SOURCE_ID
        defaultArticleShouldNotBeFound("sourceID.greaterThan=" + DEFAULT_SOURCE_ID);

        // Get all the articleList where sourceID is greater than SMALLER_SOURCE_ID
        defaultArticleShouldBeFound("sourceID.greaterThan=" + SMALLER_SOURCE_ID);
    }


    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate equals to DEFAULT_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.equals=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate equals to UPDATED_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.equals=" + UPDATED_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate not equals to DEFAULT_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.notEquals=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate not equals to UPDATED_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.notEquals=" + UPDATED_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate in DEFAULT_SOURCE_DATE or UPDATED_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.in=" + DEFAULT_SOURCE_DATE + "," + UPDATED_SOURCE_DATE);

        // Get all the articleList where sourceDate equals to UPDATED_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.in=" + UPDATED_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate is not null
        defaultArticleShouldBeFound("sourceDate.specified=true");

        // Get all the articleList where sourceDate is null
        defaultArticleShouldNotBeFound("sourceDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate is greater than or equal to DEFAULT_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.greaterThanOrEqual=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate is greater than or equal to UPDATED_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.greaterThanOrEqual=" + UPDATED_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate is less than or equal to DEFAULT_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.lessThanOrEqual=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate is less than or equal to SMALLER_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.lessThanOrEqual=" + SMALLER_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate is less than DEFAULT_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.lessThan=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate is less than UPDATED_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.lessThan=" + UPDATED_SOURCE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceDate is greater than DEFAULT_SOURCE_DATE
        defaultArticleShouldNotBeFound("sourceDate.greaterThan=" + DEFAULT_SOURCE_DATE);

        // Get all the articleList where sourceDate is greater than SMALLER_SOURCE_DATE
        defaultArticleShouldBeFound("sourceDate.greaterThan=" + SMALLER_SOURCE_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesBySourceTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle equals to DEFAULT_SOURCE_TITLE
        defaultArticleShouldBeFound("sourceTitle.equals=" + DEFAULT_SOURCE_TITLE);

        // Get all the articleList where sourceTitle equals to UPDATED_SOURCE_TITLE
        defaultArticleShouldNotBeFound("sourceTitle.equals=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle not equals to DEFAULT_SOURCE_TITLE
        defaultArticleShouldNotBeFound("sourceTitle.notEquals=" + DEFAULT_SOURCE_TITLE);

        // Get all the articleList where sourceTitle not equals to UPDATED_SOURCE_TITLE
        defaultArticleShouldBeFound("sourceTitle.notEquals=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceTitleIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle in DEFAULT_SOURCE_TITLE or UPDATED_SOURCE_TITLE
        defaultArticleShouldBeFound("sourceTitle.in=" + DEFAULT_SOURCE_TITLE + "," + UPDATED_SOURCE_TITLE);

        // Get all the articleList where sourceTitle equals to UPDATED_SOURCE_TITLE
        defaultArticleShouldNotBeFound("sourceTitle.in=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle is not null
        defaultArticleShouldBeFound("sourceTitle.specified=true");

        // Get all the articleList where sourceTitle is null
        defaultArticleShouldNotBeFound("sourceTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesBySourceTitleContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle contains DEFAULT_SOURCE_TITLE
        defaultArticleShouldBeFound("sourceTitle.contains=" + DEFAULT_SOURCE_TITLE);

        // Get all the articleList where sourceTitle contains UPDATED_SOURCE_TITLE
        defaultArticleShouldNotBeFound("sourceTitle.contains=" + UPDATED_SOURCE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceTitleNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceTitle does not contain DEFAULT_SOURCE_TITLE
        defaultArticleShouldNotBeFound("sourceTitle.doesNotContain=" + DEFAULT_SOURCE_TITLE);

        // Get all the articleList where sourceTitle does not contain UPDATED_SOURCE_TITLE
        defaultArticleShouldBeFound("sourceTitle.doesNotContain=" + UPDATED_SOURCE_TITLE);
    }


    @Test
    @Transactional
    public void getAllArticlesBySourceAbstractIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract equals to DEFAULT_SOURCE_ABSTRACT
        defaultArticleShouldBeFound("sourceAbstract.equals=" + DEFAULT_SOURCE_ABSTRACT);

        // Get all the articleList where sourceAbstract equals to UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldNotBeFound("sourceAbstract.equals=" + UPDATED_SOURCE_ABSTRACT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceAbstractIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract not equals to DEFAULT_SOURCE_ABSTRACT
        defaultArticleShouldNotBeFound("sourceAbstract.notEquals=" + DEFAULT_SOURCE_ABSTRACT);

        // Get all the articleList where sourceAbstract not equals to UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldBeFound("sourceAbstract.notEquals=" + UPDATED_SOURCE_ABSTRACT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceAbstractIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract in DEFAULT_SOURCE_ABSTRACT or UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldBeFound("sourceAbstract.in=" + DEFAULT_SOURCE_ABSTRACT + "," + UPDATED_SOURCE_ABSTRACT);

        // Get all the articleList where sourceAbstract equals to UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldNotBeFound("sourceAbstract.in=" + UPDATED_SOURCE_ABSTRACT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceAbstractIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract is not null
        defaultArticleShouldBeFound("sourceAbstract.specified=true");

        // Get all the articleList where sourceAbstract is null
        defaultArticleShouldNotBeFound("sourceAbstract.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesBySourceAbstractContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract contains DEFAULT_SOURCE_ABSTRACT
        defaultArticleShouldBeFound("sourceAbstract.contains=" + DEFAULT_SOURCE_ABSTRACT);

        // Get all the articleList where sourceAbstract contains UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldNotBeFound("sourceAbstract.contains=" + UPDATED_SOURCE_ABSTRACT);
    }

    @Test
    @Transactional
    public void getAllArticlesBySourceAbstractNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where sourceAbstract does not contain DEFAULT_SOURCE_ABSTRACT
        defaultArticleShouldNotBeFound("sourceAbstract.doesNotContain=" + DEFAULT_SOURCE_ABSTRACT);

        // Get all the articleList where sourceAbstract does not contain UPDATED_SOURCE_ABSTRACT
        defaultArticleShouldBeFound("sourceAbstract.doesNotContain=" + UPDATED_SOURCE_ABSTRACT);
    }


    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate equals to DEFAULT_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.equals=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate equals to UPDATED_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.equals=" + UPDATED_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate not equals to DEFAULT_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.notEquals=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate not equals to UPDATED_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.notEquals=" + UPDATED_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate in DEFAULT_PUBMED_DATE or UPDATED_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.in=" + DEFAULT_PUBMED_DATE + "," + UPDATED_PUBMED_DATE);

        // Get all the articleList where pubmedDate equals to UPDATED_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.in=" + UPDATED_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate is not null
        defaultArticleShouldBeFound("pubmedDate.specified=true");

        // Get all the articleList where pubmedDate is null
        defaultArticleShouldNotBeFound("pubmedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate is greater than or equal to DEFAULT_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.greaterThanOrEqual=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate is greater than or equal to UPDATED_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.greaterThanOrEqual=" + UPDATED_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate is less than or equal to DEFAULT_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.lessThanOrEqual=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate is less than or equal to SMALLER_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.lessThanOrEqual=" + SMALLER_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate is less than DEFAULT_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.lessThan=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate is less than UPDATED_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.lessThan=" + UPDATED_PUBMED_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByPubmedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where pubmedDate is greater than DEFAULT_PUBMED_DATE
        defaultArticleShouldNotBeFound("pubmedDate.greaterThan=" + DEFAULT_PUBMED_DATE);

        // Get all the articleList where pubmedDate is greater than SMALLER_PUBMED_DATE
        defaultArticleShouldBeFound("pubmedDate.greaterThan=" + SMALLER_PUBMED_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate equals to DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.equals=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate equals to UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.equals=" + UPDATED_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate not equals to DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.notEquals=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate not equals to UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.notEquals=" + UPDATED_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate in DEFAULT_OFFICIAL_PUB_DATE or UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.in=" + DEFAULT_OFFICIAL_PUB_DATE + "," + UPDATED_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate equals to UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.in=" + UPDATED_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate is not null
        defaultArticleShouldBeFound("officialPubDate.specified=true");

        // Get all the articleList where officialPubDate is null
        defaultArticleShouldNotBeFound("officialPubDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate is greater than or equal to DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.greaterThanOrEqual=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate is greater than or equal to UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.greaterThanOrEqual=" + UPDATED_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate is less than or equal to DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.lessThanOrEqual=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate is less than or equal to SMALLER_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.lessThanOrEqual=" + SMALLER_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate is less than DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.lessThan=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate is less than UPDATED_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.lessThan=" + UPDATED_OFFICIAL_PUB_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByOfficialPubDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where officialPubDate is greater than DEFAULT_OFFICIAL_PUB_DATE
        defaultArticleShouldNotBeFound("officialPubDate.greaterThan=" + DEFAULT_OFFICIAL_PUB_DATE);

        // Get all the articleList where officialPubDate is greater than SMALLER_OFFICIAL_PUB_DATE
        defaultArticleShouldBeFound("officialPubDate.greaterThan=" + SMALLER_OFFICIAL_PUB_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByDoiIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi equals to DEFAULT_DOI
        defaultArticleShouldBeFound("doi.equals=" + DEFAULT_DOI);

        // Get all the articleList where doi equals to UPDATED_DOI
        defaultArticleShouldNotBeFound("doi.equals=" + UPDATED_DOI);
    }

    @Test
    @Transactional
    public void getAllArticlesByDoiIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi not equals to DEFAULT_DOI
        defaultArticleShouldNotBeFound("doi.notEquals=" + DEFAULT_DOI);

        // Get all the articleList where doi not equals to UPDATED_DOI
        defaultArticleShouldBeFound("doi.notEquals=" + UPDATED_DOI);
    }

    @Test
    @Transactional
    public void getAllArticlesByDoiIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi in DEFAULT_DOI or UPDATED_DOI
        defaultArticleShouldBeFound("doi.in=" + DEFAULT_DOI + "," + UPDATED_DOI);

        // Get all the articleList where doi equals to UPDATED_DOI
        defaultArticleShouldNotBeFound("doi.in=" + UPDATED_DOI);
    }

    @Test
    @Transactional
    public void getAllArticlesByDoiIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi is not null
        defaultArticleShouldBeFound("doi.specified=true");

        // Get all the articleList where doi is null
        defaultArticleShouldNotBeFound("doi.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByDoiContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi contains DEFAULT_DOI
        defaultArticleShouldBeFound("doi.contains=" + DEFAULT_DOI);

        // Get all the articleList where doi contains UPDATED_DOI
        defaultArticleShouldNotBeFound("doi.contains=" + UPDATED_DOI);
    }

    @Test
    @Transactional
    public void getAllArticlesByDoiNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where doi does not contain DEFAULT_DOI
        defaultArticleShouldNotBeFound("doi.doesNotContain=" + DEFAULT_DOI);

        // Get all the articleList where doi does not contain UPDATED_DOI
        defaultArticleShouldBeFound("doi.doesNotContain=" + UPDATED_DOI);
    }


    @Test
    @Transactional
    public void getAllArticlesByJournalIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal equals to DEFAULT_JOURNAL
        defaultArticleShouldBeFound("journal.equals=" + DEFAULT_JOURNAL);

        // Get all the articleList where journal equals to UPDATED_JOURNAL
        defaultArticleShouldNotBeFound("journal.equals=" + UPDATED_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByJournalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal not equals to DEFAULT_JOURNAL
        defaultArticleShouldNotBeFound("journal.notEquals=" + DEFAULT_JOURNAL);

        // Get all the articleList where journal not equals to UPDATED_JOURNAL
        defaultArticleShouldBeFound("journal.notEquals=" + UPDATED_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByJournalIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal in DEFAULT_JOURNAL or UPDATED_JOURNAL
        defaultArticleShouldBeFound("journal.in=" + DEFAULT_JOURNAL + "," + UPDATED_JOURNAL);

        // Get all the articleList where journal equals to UPDATED_JOURNAL
        defaultArticleShouldNotBeFound("journal.in=" + UPDATED_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByJournalIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal is not null
        defaultArticleShouldBeFound("journal.specified=true");

        // Get all the articleList where journal is null
        defaultArticleShouldNotBeFound("journal.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByJournalContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal contains DEFAULT_JOURNAL
        defaultArticleShouldBeFound("journal.contains=" + DEFAULT_JOURNAL);

        // Get all the articleList where journal contains UPDATED_JOURNAL
        defaultArticleShouldNotBeFound("journal.contains=" + UPDATED_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByJournalNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where journal does not contain DEFAULT_JOURNAL
        defaultArticleShouldNotBeFound("journal.doesNotContain=" + DEFAULT_JOURNAL);

        // Get all the articleList where journal does not contain UPDATED_JOURNAL
        defaultArticleShouldBeFound("journal.doesNotContain=" + UPDATED_JOURNAL);
    }


    @Test
    @Transactional
    public void getAllArticlesByCitationIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation equals to DEFAULT_CITATION
        defaultArticleShouldBeFound("citation.equals=" + DEFAULT_CITATION);

        // Get all the articleList where citation equals to UPDATED_CITATION
        defaultArticleShouldNotBeFound("citation.equals=" + UPDATED_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByCitationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation not equals to DEFAULT_CITATION
        defaultArticleShouldNotBeFound("citation.notEquals=" + DEFAULT_CITATION);

        // Get all the articleList where citation not equals to UPDATED_CITATION
        defaultArticleShouldBeFound("citation.notEquals=" + UPDATED_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByCitationIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation in DEFAULT_CITATION or UPDATED_CITATION
        defaultArticleShouldBeFound("citation.in=" + DEFAULT_CITATION + "," + UPDATED_CITATION);

        // Get all the articleList where citation equals to UPDATED_CITATION
        defaultArticleShouldNotBeFound("citation.in=" + UPDATED_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByCitationIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation is not null
        defaultArticleShouldBeFound("citation.specified=true");

        // Get all the articleList where citation is null
        defaultArticleShouldNotBeFound("citation.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByCitationContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation contains DEFAULT_CITATION
        defaultArticleShouldBeFound("citation.contains=" + DEFAULT_CITATION);

        // Get all the articleList where citation contains UPDATED_CITATION
        defaultArticleShouldNotBeFound("citation.contains=" + UPDATED_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByCitationNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where citation does not contain DEFAULT_CITATION
        defaultArticleShouldNotBeFound("citation.doesNotContain=" + DEFAULT_CITATION);

        // Get all the articleList where citation does not contain UPDATED_CITATION
        defaultArticleShouldBeFound("citation.doesNotContain=" + UPDATED_CITATION);
    }


    @Test
    @Transactional
    public void getAllArticlesByReviewStateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where reviewState equals to DEFAULT_REVIEW_STATE
        defaultArticleShouldBeFound("reviewState.equals=" + DEFAULT_REVIEW_STATE);

        // Get all the articleList where reviewState equals to UPDATED_REVIEW_STATE
        defaultArticleShouldNotBeFound("reviewState.equals=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByReviewStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where reviewState not equals to DEFAULT_REVIEW_STATE
        defaultArticleShouldNotBeFound("reviewState.notEquals=" + DEFAULT_REVIEW_STATE);

        // Get all the articleList where reviewState not equals to UPDATED_REVIEW_STATE
        defaultArticleShouldBeFound("reviewState.notEquals=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByReviewStateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where reviewState in DEFAULT_REVIEW_STATE or UPDATED_REVIEW_STATE
        defaultArticleShouldBeFound("reviewState.in=" + DEFAULT_REVIEW_STATE + "," + UPDATED_REVIEW_STATE);

        // Get all the articleList where reviewState equals to UPDATED_REVIEW_STATE
        defaultArticleShouldNotBeFound("reviewState.in=" + UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByReviewStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where reviewState is not null
        defaultArticleShouldBeFound("reviewState.specified=true");

        // Get all the articleList where reviewState is null
        defaultArticleShouldNotBeFound("reviewState.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Revision revision = RevisionResourceIT.createEntity(em);
        em.persist(revision);
        em.flush();
        article.addRevision(revision);
        articleRepository.saveAndFlush(article);
        Long revisionId = revision.getId();

        // Get all the articleList where revision equals to revisionId
        defaultArticleShouldBeFound("revisionId.equals=" + revisionId);

        // Get all the articleList where revision equals to revisionId + 1
        defaultArticleShouldNotBeFound("revisionId.equals=" + (revisionId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesByCntsourceIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        ContentSource cntsource = ContentSourceResourceIT.createEntity(em);
        em.persist(cntsource);
        em.flush();
        article.setCntsource(cntsource);
        articleRepository.saveAndFlush(article);
        Long cntsourceId = cntsource.getId();

        // Get all the articleList where cntsource equals to cntsourceId
        defaultArticleShouldBeFound("cntsourceId.equals=" + cntsourceId);

        // Get all the articleList where cntsource equals to cntsourceId + 1
        defaultArticleShouldNotBeFound("cntsourceId.equals=" + (cntsourceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceID").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].sourceDate").value(hasItem(DEFAULT_SOURCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceAbstract").value(hasItem(DEFAULT_SOURCE_ABSTRACT)))
            .andExpect(jsonPath("$.[*].pubmedDate").value(hasItem(DEFAULT_PUBMED_DATE.toString())))
            .andExpect(jsonPath("$.[*].officialPubDate").value(hasItem(DEFAULT_OFFICIAL_PUB_DATE.toString())))
            .andExpect(jsonPath("$.[*].doi").value(hasItem(DEFAULT_DOI)))
            .andExpect(jsonPath("$.[*].journal").value(hasItem(DEFAULT_JOURNAL)))
            .andExpect(jsonPath("$.[*].citation").value(hasItem(DEFAULT_CITATION)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].reviewState").value(hasItem(DEFAULT_REVIEW_STATE.toString())));

        // Check, that the count call also returns 1
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc.perform(get("/api/articles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get("/api/articles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).get();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .sourceID(UPDATED_SOURCE_ID)
            .sourceDate(UPDATED_SOURCE_DATE)
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceAbstract(UPDATED_SOURCE_ABSTRACT)
            .pubmedDate(UPDATED_PUBMED_DATE)
            .officialPubDate(UPDATED_OFFICIAL_PUB_DATE)
            .doi(UPDATED_DOI)
            .journal(UPDATED_JOURNAL)
            .citation(UPDATED_CITATION)
            .keywords(UPDATED_KEYWORDS)
            .reviewState(UPDATED_REVIEW_STATE);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticle)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getSourceID()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testArticle.getSourceDate()).isEqualTo(UPDATED_SOURCE_DATE);
        assertThat(testArticle.getSourceTitle()).isEqualTo(UPDATED_SOURCE_TITLE);
        assertThat(testArticle.getSourceAbstract()).isEqualTo(UPDATED_SOURCE_ABSTRACT);
        assertThat(testArticle.getPubmedDate()).isEqualTo(UPDATED_PUBMED_DATE);
        assertThat(testArticle.getOfficialPubDate()).isEqualTo(UPDATED_OFFICIAL_PUB_DATE);
        assertThat(testArticle.getDoi()).isEqualTo(UPDATED_DOI);
        assertThat(testArticle.getJournal()).isEqualTo(UPDATED_JOURNAL);
        assertThat(testArticle.getCitation()).isEqualTo(UPDATED_CITATION);
        assertThat(testArticle.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testArticle.getReviewState()).isEqualTo(UPDATED_REVIEW_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingArticle() throws Exception {
        int databaseSizeBeforeUpdate = articleRepository.findAll().size();

        // Create the Article

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArticle() throws Exception {
        // Initialize the database
        articleService.save(article);

        int databaseSizeBeforeDelete = articleRepository.findAll().size();

        // Delete the article
        restArticleMockMvc.perform(delete("/api/articles/{id}", article.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
