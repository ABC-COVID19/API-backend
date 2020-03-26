package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Article;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.PublicationSource;
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

import pt.tech4covid.domain.enumeration.ContentSource;
import pt.tech4covid.domain.enumeration.ReviewState;
/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ArticleResourceIT {

    private static final ContentSource DEFAULT_FETCHED_FROM = ContentSource.PubMed;
    private static final ContentSource UPDATED_FETCHED_FROM = ContentSource.PubMed;

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

    private static final LocalDate DEFAULT_IMPORT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_IMPORT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_IMPORT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_OUTBOUND_LINK = "AAAAAAAAAA";
    private static final String UPDATED_OUTBOUND_LINK = "BBBBBBBBBB";

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
            .fetchedFrom(DEFAULT_FETCHED_FROM)
            .sourceID(DEFAULT_SOURCE_ID)
            .sourceDate(DEFAULT_SOURCE_DATE)
            .sourceTitle(DEFAULT_SOURCE_TITLE)
            .sourceAbstract(DEFAULT_SOURCE_ABSTRACT)
            .importDate(DEFAULT_IMPORT_DATE)
            .outboundLink(DEFAULT_OUTBOUND_LINK)
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
            .fetchedFrom(UPDATED_FETCHED_FROM)
            .sourceID(UPDATED_SOURCE_ID)
            .sourceDate(UPDATED_SOURCE_DATE)
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceAbstract(UPDATED_SOURCE_ABSTRACT)
            .importDate(UPDATED_IMPORT_DATE)
            .outboundLink(UPDATED_OUTBOUND_LINK)
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
        assertThat(testArticle.getFetchedFrom()).isEqualTo(DEFAULT_FETCHED_FROM);
        assertThat(testArticle.getSourceID()).isEqualTo(DEFAULT_SOURCE_ID);
        assertThat(testArticle.getSourceDate()).isEqualTo(DEFAULT_SOURCE_DATE);
        assertThat(testArticle.getSourceTitle()).isEqualTo(DEFAULT_SOURCE_TITLE);
        assertThat(testArticle.getSourceAbstract()).isEqualTo(DEFAULT_SOURCE_ABSTRACT);
        assertThat(testArticle.getImportDate()).isEqualTo(DEFAULT_IMPORT_DATE);
        assertThat(testArticle.getOutboundLink()).isEqualTo(DEFAULT_OUTBOUND_LINK);
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
    public void getAllArticles() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].fetchedFrom").value(hasItem(DEFAULT_FETCHED_FROM.toString())))
            .andExpect(jsonPath("$.[*].sourceID").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].sourceDate").value(hasItem(DEFAULT_SOURCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceAbstract").value(hasItem(DEFAULT_SOURCE_ABSTRACT)))
            .andExpect(jsonPath("$.[*].importDate").value(hasItem(DEFAULT_IMPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].outboundLink").value(hasItem(DEFAULT_OUTBOUND_LINK)))
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
            .andExpect(jsonPath("$.fetchedFrom").value(DEFAULT_FETCHED_FROM.toString()))
            .andExpect(jsonPath("$.sourceID").value(DEFAULT_SOURCE_ID))
            .andExpect(jsonPath("$.sourceDate").value(DEFAULT_SOURCE_DATE.toString()))
            .andExpect(jsonPath("$.sourceTitle").value(DEFAULT_SOURCE_TITLE))
            .andExpect(jsonPath("$.sourceAbstract").value(DEFAULT_SOURCE_ABSTRACT))
            .andExpect(jsonPath("$.importDate").value(DEFAULT_IMPORT_DATE.toString()))
            .andExpect(jsonPath("$.outboundLink").value(DEFAULT_OUTBOUND_LINK))
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
    public void getAllArticlesByFetchedFromIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchedFrom equals to DEFAULT_FETCHED_FROM
        defaultArticleShouldBeFound("fetchedFrom.equals=" + DEFAULT_FETCHED_FROM);

        // Get all the articleList where fetchedFrom equals to UPDATED_FETCHED_FROM
        defaultArticleShouldNotBeFound("fetchedFrom.equals=" + UPDATED_FETCHED_FROM);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchedFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchedFrom not equals to DEFAULT_FETCHED_FROM
        defaultArticleShouldNotBeFound("fetchedFrom.notEquals=" + DEFAULT_FETCHED_FROM);

        // Get all the articleList where fetchedFrom not equals to UPDATED_FETCHED_FROM
        defaultArticleShouldBeFound("fetchedFrom.notEquals=" + UPDATED_FETCHED_FROM);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchedFromIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchedFrom in DEFAULT_FETCHED_FROM or UPDATED_FETCHED_FROM
        defaultArticleShouldBeFound("fetchedFrom.in=" + DEFAULT_FETCHED_FROM + "," + UPDATED_FETCHED_FROM);

        // Get all the articleList where fetchedFrom equals to UPDATED_FETCHED_FROM
        defaultArticleShouldNotBeFound("fetchedFrom.in=" + UPDATED_FETCHED_FROM);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchedFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchedFrom is not null
        defaultArticleShouldBeFound("fetchedFrom.specified=true");

        // Get all the articleList where fetchedFrom is null
        defaultArticleShouldNotBeFound("fetchedFrom.specified=false");
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
    public void getAllArticlesByImportDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate equals to DEFAULT_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.equals=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate equals to UPDATED_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.equals=" + UPDATED_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate not equals to DEFAULT_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.notEquals=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate not equals to UPDATED_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.notEquals=" + UPDATED_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate in DEFAULT_IMPORT_DATE or UPDATED_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.in=" + DEFAULT_IMPORT_DATE + "," + UPDATED_IMPORT_DATE);

        // Get all the articleList where importDate equals to UPDATED_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.in=" + UPDATED_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate is not null
        defaultArticleShouldBeFound("importDate.specified=true");

        // Get all the articleList where importDate is null
        defaultArticleShouldNotBeFound("importDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate is greater than or equal to DEFAULT_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.greaterThanOrEqual=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate is greater than or equal to UPDATED_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.greaterThanOrEqual=" + UPDATED_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate is less than or equal to DEFAULT_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.lessThanOrEqual=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate is less than or equal to SMALLER_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.lessThanOrEqual=" + SMALLER_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate is less than DEFAULT_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.lessThan=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate is less than UPDATED_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.lessThan=" + UPDATED_IMPORT_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByImportDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where importDate is greater than DEFAULT_IMPORT_DATE
        defaultArticleShouldNotBeFound("importDate.greaterThan=" + DEFAULT_IMPORT_DATE);

        // Get all the articleList where importDate is greater than SMALLER_IMPORT_DATE
        defaultArticleShouldBeFound("importDate.greaterThan=" + SMALLER_IMPORT_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByOutboundLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink equals to DEFAULT_OUTBOUND_LINK
        defaultArticleShouldBeFound("outboundLink.equals=" + DEFAULT_OUTBOUND_LINK);

        // Get all the articleList where outboundLink equals to UPDATED_OUTBOUND_LINK
        defaultArticleShouldNotBeFound("outboundLink.equals=" + UPDATED_OUTBOUND_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByOutboundLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink not equals to DEFAULT_OUTBOUND_LINK
        defaultArticleShouldNotBeFound("outboundLink.notEquals=" + DEFAULT_OUTBOUND_LINK);

        // Get all the articleList where outboundLink not equals to UPDATED_OUTBOUND_LINK
        defaultArticleShouldBeFound("outboundLink.notEquals=" + UPDATED_OUTBOUND_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByOutboundLinkIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink in DEFAULT_OUTBOUND_LINK or UPDATED_OUTBOUND_LINK
        defaultArticleShouldBeFound("outboundLink.in=" + DEFAULT_OUTBOUND_LINK + "," + UPDATED_OUTBOUND_LINK);

        // Get all the articleList where outboundLink equals to UPDATED_OUTBOUND_LINK
        defaultArticleShouldNotBeFound("outboundLink.in=" + UPDATED_OUTBOUND_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByOutboundLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink is not null
        defaultArticleShouldBeFound("outboundLink.specified=true");

        // Get all the articleList where outboundLink is null
        defaultArticleShouldNotBeFound("outboundLink.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByOutboundLinkContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink contains DEFAULT_OUTBOUND_LINK
        defaultArticleShouldBeFound("outboundLink.contains=" + DEFAULT_OUTBOUND_LINK);

        // Get all the articleList where outboundLink contains UPDATED_OUTBOUND_LINK
        defaultArticleShouldNotBeFound("outboundLink.contains=" + UPDATED_OUTBOUND_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByOutboundLinkNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where outboundLink does not contain DEFAULT_OUTBOUND_LINK
        defaultArticleShouldNotBeFound("outboundLink.doesNotContain=" + DEFAULT_OUTBOUND_LINK);

        // Get all the articleList where outboundLink does not contain UPDATED_OUTBOUND_LINK
        defaultArticleShouldBeFound("outboundLink.doesNotContain=" + UPDATED_OUTBOUND_LINK);
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
    public void getAllArticlesByPubNameIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        PublicationSource pubName = PublicationSourceResourceIT.createEntity(em);
        em.persist(pubName);
        em.flush();
        article.setPubName(pubName);
        articleRepository.saveAndFlush(article);
        Long pubNameId = pubName.getId();

        // Get all the articleList where pubName equals to pubNameId
        defaultArticleShouldBeFound("pubNameId.equals=" + pubNameId);

        // Get all the articleList where pubName equals to pubNameId + 1
        defaultArticleShouldNotBeFound("pubNameId.equals=" + (pubNameId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].fetchedFrom").value(hasItem(DEFAULT_FETCHED_FROM.toString())))
            .andExpect(jsonPath("$.[*].sourceID").value(hasItem(DEFAULT_SOURCE_ID)))
            .andExpect(jsonPath("$.[*].sourceDate").value(hasItem(DEFAULT_SOURCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].sourceTitle").value(hasItem(DEFAULT_SOURCE_TITLE)))
            .andExpect(jsonPath("$.[*].sourceAbstract").value(hasItem(DEFAULT_SOURCE_ABSTRACT)))
            .andExpect(jsonPath("$.[*].importDate").value(hasItem(DEFAULT_IMPORT_DATE.toString())))
            .andExpect(jsonPath("$.[*].outboundLink").value(hasItem(DEFAULT_OUTBOUND_LINK)))
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
            .fetchedFrom(UPDATED_FETCHED_FROM)
            .sourceID(UPDATED_SOURCE_ID)
            .sourceDate(UPDATED_SOURCE_DATE)
            .sourceTitle(UPDATED_SOURCE_TITLE)
            .sourceAbstract(UPDATED_SOURCE_ABSTRACT)
            .importDate(UPDATED_IMPORT_DATE)
            .outboundLink(UPDATED_OUTBOUND_LINK)
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
        assertThat(testArticle.getFetchedFrom()).isEqualTo(UPDATED_FETCHED_FROM);
        assertThat(testArticle.getSourceID()).isEqualTo(UPDATED_SOURCE_ID);
        assertThat(testArticle.getSourceDate()).isEqualTo(UPDATED_SOURCE_DATE);
        assertThat(testArticle.getSourceTitle()).isEqualTo(UPDATED_SOURCE_TITLE);
        assertThat(testArticle.getSourceAbstract()).isEqualTo(UPDATED_SOURCE_ABSTRACT);
        assertThat(testArticle.getImportDate()).isEqualTo(UPDATED_IMPORT_DATE);
        assertThat(testArticle.getOutboundLink()).isEqualTo(UPDATED_OUTBOUND_LINK);
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
