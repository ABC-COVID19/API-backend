package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Article;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.SourceRepo;
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

/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ArticleResourceIT {

    private static final Integer DEFAULT_REPO_ARTICLE_ID = 1;
    private static final Integer UPDATED_REPO_ARTICLE_ID = 2;
    private static final Integer SMALLER_REPO_ARTICLE_ID = 1 - 1;

    private static final LocalDate DEFAULT_REPO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPO_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_REPO_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_REPO_KEYWORDS = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_ABSTRACT = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_ABSTRACT = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_JOURNAL = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_JOURNAL = "BBBBBBBBBB";

    private static final String DEFAULT_ARTICLE_CITATION = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_CITATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FETCH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FETCH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FETCH_DATE = LocalDate.ofEpochDay(-1L);

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
            .repoArticleId(DEFAULT_REPO_ARTICLE_ID)
            .repoDate(DEFAULT_REPO_DATE)
            .repoKeywords(DEFAULT_REPO_KEYWORDS)
            .articleDate(DEFAULT_ARTICLE_DATE)
            .articleTitle(DEFAULT_ARTICLE_TITLE)
            .articleAbstract(DEFAULT_ARTICLE_ABSTRACT)
            .articleLink(DEFAULT_ARTICLE_LINK)
            .articleJournal(DEFAULT_ARTICLE_JOURNAL)
            .articleCitation(DEFAULT_ARTICLE_CITATION)
            .fetchDate(DEFAULT_FETCH_DATE);
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
            .repoArticleId(UPDATED_REPO_ARTICLE_ID)
            .repoDate(UPDATED_REPO_DATE)
            .repoKeywords(UPDATED_REPO_KEYWORDS)
            .articleDate(UPDATED_ARTICLE_DATE)
            .articleTitle(UPDATED_ARTICLE_TITLE)
            .articleAbstract(UPDATED_ARTICLE_ABSTRACT)
            .articleLink(UPDATED_ARTICLE_LINK)
            .articleJournal(UPDATED_ARTICLE_JOURNAL)
            .articleCitation(UPDATED_ARTICLE_CITATION)
            .fetchDate(UPDATED_FETCH_DATE);
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
        assertThat(testArticle.getRepoArticleId()).isEqualTo(DEFAULT_REPO_ARTICLE_ID);
        assertThat(testArticle.getRepoDate()).isEqualTo(DEFAULT_REPO_DATE);
        assertThat(testArticle.getRepoKeywords()).isEqualTo(DEFAULT_REPO_KEYWORDS);
        assertThat(testArticle.getArticleDate()).isEqualTo(DEFAULT_ARTICLE_DATE);
        assertThat(testArticle.getArticleTitle()).isEqualTo(DEFAULT_ARTICLE_TITLE);
        assertThat(testArticle.getArticleAbstract()).isEqualTo(DEFAULT_ARTICLE_ABSTRACT);
        assertThat(testArticle.getArticleLink()).isEqualTo(DEFAULT_ARTICLE_LINK);
        assertThat(testArticle.getArticleJournal()).isEqualTo(DEFAULT_ARTICLE_JOURNAL);
        assertThat(testArticle.getArticleCitation()).isEqualTo(DEFAULT_ARTICLE_CITATION);
        assertThat(testArticle.getFetchDate()).isEqualTo(DEFAULT_FETCH_DATE);
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
            .andExpect(jsonPath("$.[*].repoArticleId").value(hasItem(DEFAULT_REPO_ARTICLE_ID)))
            .andExpect(jsonPath("$.[*].repoDate").value(hasItem(DEFAULT_REPO_DATE.toString())))
            .andExpect(jsonPath("$.[*].repoKeywords").value(hasItem(DEFAULT_REPO_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].articleDate").value(hasItem(DEFAULT_ARTICLE_DATE)))
            .andExpect(jsonPath("$.[*].articleTitle").value(hasItem(DEFAULT_ARTICLE_TITLE)))
            .andExpect(jsonPath("$.[*].articleAbstract").value(hasItem(DEFAULT_ARTICLE_ABSTRACT.toString())))
            .andExpect(jsonPath("$.[*].articleLink").value(hasItem(DEFAULT_ARTICLE_LINK)))
            .andExpect(jsonPath("$.[*].articleJournal").value(hasItem(DEFAULT_ARTICLE_JOURNAL)))
            .andExpect(jsonPath("$.[*].articleCitation").value(hasItem(DEFAULT_ARTICLE_CITATION)))
            .andExpect(jsonPath("$.[*].fetchDate").value(hasItem(DEFAULT_FETCH_DATE.toString())));
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
            .andExpect(jsonPath("$.repoArticleId").value(DEFAULT_REPO_ARTICLE_ID))
            .andExpect(jsonPath("$.repoDate").value(DEFAULT_REPO_DATE.toString()))
            .andExpect(jsonPath("$.repoKeywords").value(DEFAULT_REPO_KEYWORDS.toString()))
            .andExpect(jsonPath("$.articleDate").value(DEFAULT_ARTICLE_DATE))
            .andExpect(jsonPath("$.articleTitle").value(DEFAULT_ARTICLE_TITLE))
            .andExpect(jsonPath("$.articleAbstract").value(DEFAULT_ARTICLE_ABSTRACT.toString()))
            .andExpect(jsonPath("$.articleLink").value(DEFAULT_ARTICLE_LINK))
            .andExpect(jsonPath("$.articleJournal").value(DEFAULT_ARTICLE_JOURNAL))
            .andExpect(jsonPath("$.articleCitation").value(DEFAULT_ARTICLE_CITATION))
            .andExpect(jsonPath("$.fetchDate").value(DEFAULT_FETCH_DATE.toString()));
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
    public void getAllArticlesByRepoArticleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId equals to DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.equals=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId equals to UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.equals=" + UPDATED_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId not equals to DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.notEquals=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId not equals to UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.notEquals=" + UPDATED_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId in DEFAULT_REPO_ARTICLE_ID or UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.in=" + DEFAULT_REPO_ARTICLE_ID + "," + UPDATED_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId equals to UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.in=" + UPDATED_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId is not null
        defaultArticleShouldBeFound("repoArticleId.specified=true");

        // Get all the articleList where repoArticleId is null
        defaultArticleShouldNotBeFound("repoArticleId.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId is greater than or equal to DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.greaterThanOrEqual=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId is greater than or equal to UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.greaterThanOrEqual=" + UPDATED_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId is less than or equal to DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.lessThanOrEqual=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId is less than or equal to SMALLER_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.lessThanOrEqual=" + SMALLER_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId is less than DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.lessThan=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId is less than UPDATED_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.lessThan=" + UPDATED_REPO_ARTICLE_ID);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoArticleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoArticleId is greater than DEFAULT_REPO_ARTICLE_ID
        defaultArticleShouldNotBeFound("repoArticleId.greaterThan=" + DEFAULT_REPO_ARTICLE_ID);

        // Get all the articleList where repoArticleId is greater than SMALLER_REPO_ARTICLE_ID
        defaultArticleShouldBeFound("repoArticleId.greaterThan=" + SMALLER_REPO_ARTICLE_ID);
    }


    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate equals to DEFAULT_REPO_DATE
        defaultArticleShouldBeFound("repoDate.equals=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate equals to UPDATED_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.equals=" + UPDATED_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate not equals to DEFAULT_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.notEquals=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate not equals to UPDATED_REPO_DATE
        defaultArticleShouldBeFound("repoDate.notEquals=" + UPDATED_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate in DEFAULT_REPO_DATE or UPDATED_REPO_DATE
        defaultArticleShouldBeFound("repoDate.in=" + DEFAULT_REPO_DATE + "," + UPDATED_REPO_DATE);

        // Get all the articleList where repoDate equals to UPDATED_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.in=" + UPDATED_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate is not null
        defaultArticleShouldBeFound("repoDate.specified=true");

        // Get all the articleList where repoDate is null
        defaultArticleShouldNotBeFound("repoDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate is greater than or equal to DEFAULT_REPO_DATE
        defaultArticleShouldBeFound("repoDate.greaterThanOrEqual=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate is greater than or equal to UPDATED_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.greaterThanOrEqual=" + UPDATED_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate is less than or equal to DEFAULT_REPO_DATE
        defaultArticleShouldBeFound("repoDate.lessThanOrEqual=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate is less than or equal to SMALLER_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.lessThanOrEqual=" + SMALLER_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate is less than DEFAULT_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.lessThan=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate is less than UPDATED_REPO_DATE
        defaultArticleShouldBeFound("repoDate.lessThan=" + UPDATED_REPO_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByRepoDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where repoDate is greater than DEFAULT_REPO_DATE
        defaultArticleShouldNotBeFound("repoDate.greaterThan=" + DEFAULT_REPO_DATE);

        // Get all the articleList where repoDate is greater than SMALLER_REPO_DATE
        defaultArticleShouldBeFound("repoDate.greaterThan=" + SMALLER_REPO_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByArticleDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate equals to DEFAULT_ARTICLE_DATE
        defaultArticleShouldBeFound("articleDate.equals=" + DEFAULT_ARTICLE_DATE);

        // Get all the articleList where articleDate equals to UPDATED_ARTICLE_DATE
        defaultArticleShouldNotBeFound("articleDate.equals=" + UPDATED_ARTICLE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate not equals to DEFAULT_ARTICLE_DATE
        defaultArticleShouldNotBeFound("articleDate.notEquals=" + DEFAULT_ARTICLE_DATE);

        // Get all the articleList where articleDate not equals to UPDATED_ARTICLE_DATE
        defaultArticleShouldBeFound("articleDate.notEquals=" + UPDATED_ARTICLE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate in DEFAULT_ARTICLE_DATE or UPDATED_ARTICLE_DATE
        defaultArticleShouldBeFound("articleDate.in=" + DEFAULT_ARTICLE_DATE + "," + UPDATED_ARTICLE_DATE);

        // Get all the articleList where articleDate equals to UPDATED_ARTICLE_DATE
        defaultArticleShouldNotBeFound("articleDate.in=" + UPDATED_ARTICLE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate is not null
        defaultArticleShouldBeFound("articleDate.specified=true");

        // Get all the articleList where articleDate is null
        defaultArticleShouldNotBeFound("articleDate.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByArticleDateContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate contains DEFAULT_ARTICLE_DATE
        defaultArticleShouldBeFound("articleDate.contains=" + DEFAULT_ARTICLE_DATE);

        // Get all the articleList where articleDate contains UPDATED_ARTICLE_DATE
        defaultArticleShouldNotBeFound("articleDate.contains=" + UPDATED_ARTICLE_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleDateNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleDate does not contain DEFAULT_ARTICLE_DATE
        defaultArticleShouldNotBeFound("articleDate.doesNotContain=" + DEFAULT_ARTICLE_DATE);

        // Get all the articleList where articleDate does not contain UPDATED_ARTICLE_DATE
        defaultArticleShouldBeFound("articleDate.doesNotContain=" + UPDATED_ARTICLE_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByArticleTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle equals to DEFAULT_ARTICLE_TITLE
        defaultArticleShouldBeFound("articleTitle.equals=" + DEFAULT_ARTICLE_TITLE);

        // Get all the articleList where articleTitle equals to UPDATED_ARTICLE_TITLE
        defaultArticleShouldNotBeFound("articleTitle.equals=" + UPDATED_ARTICLE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle not equals to DEFAULT_ARTICLE_TITLE
        defaultArticleShouldNotBeFound("articleTitle.notEquals=" + DEFAULT_ARTICLE_TITLE);

        // Get all the articleList where articleTitle not equals to UPDATED_ARTICLE_TITLE
        defaultArticleShouldBeFound("articleTitle.notEquals=" + UPDATED_ARTICLE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleTitleIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle in DEFAULT_ARTICLE_TITLE or UPDATED_ARTICLE_TITLE
        defaultArticleShouldBeFound("articleTitle.in=" + DEFAULT_ARTICLE_TITLE + "," + UPDATED_ARTICLE_TITLE);

        // Get all the articleList where articleTitle equals to UPDATED_ARTICLE_TITLE
        defaultArticleShouldNotBeFound("articleTitle.in=" + UPDATED_ARTICLE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle is not null
        defaultArticleShouldBeFound("articleTitle.specified=true");

        // Get all the articleList where articleTitle is null
        defaultArticleShouldNotBeFound("articleTitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByArticleTitleContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle contains DEFAULT_ARTICLE_TITLE
        defaultArticleShouldBeFound("articleTitle.contains=" + DEFAULT_ARTICLE_TITLE);

        // Get all the articleList where articleTitle contains UPDATED_ARTICLE_TITLE
        defaultArticleShouldNotBeFound("articleTitle.contains=" + UPDATED_ARTICLE_TITLE);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleTitleNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleTitle does not contain DEFAULT_ARTICLE_TITLE
        defaultArticleShouldNotBeFound("articleTitle.doesNotContain=" + DEFAULT_ARTICLE_TITLE);

        // Get all the articleList where articleTitle does not contain UPDATED_ARTICLE_TITLE
        defaultArticleShouldBeFound("articleTitle.doesNotContain=" + UPDATED_ARTICLE_TITLE);
    }


    @Test
    @Transactional
    public void getAllArticlesByArticleLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink equals to DEFAULT_ARTICLE_LINK
        defaultArticleShouldBeFound("articleLink.equals=" + DEFAULT_ARTICLE_LINK);

        // Get all the articleList where articleLink equals to UPDATED_ARTICLE_LINK
        defaultArticleShouldNotBeFound("articleLink.equals=" + UPDATED_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink not equals to DEFAULT_ARTICLE_LINK
        defaultArticleShouldNotBeFound("articleLink.notEquals=" + DEFAULT_ARTICLE_LINK);

        // Get all the articleList where articleLink not equals to UPDATED_ARTICLE_LINK
        defaultArticleShouldBeFound("articleLink.notEquals=" + UPDATED_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleLinkIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink in DEFAULT_ARTICLE_LINK or UPDATED_ARTICLE_LINK
        defaultArticleShouldBeFound("articleLink.in=" + DEFAULT_ARTICLE_LINK + "," + UPDATED_ARTICLE_LINK);

        // Get all the articleList where articleLink equals to UPDATED_ARTICLE_LINK
        defaultArticleShouldNotBeFound("articleLink.in=" + UPDATED_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink is not null
        defaultArticleShouldBeFound("articleLink.specified=true");

        // Get all the articleList where articleLink is null
        defaultArticleShouldNotBeFound("articleLink.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByArticleLinkContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink contains DEFAULT_ARTICLE_LINK
        defaultArticleShouldBeFound("articleLink.contains=" + DEFAULT_ARTICLE_LINK);

        // Get all the articleList where articleLink contains UPDATED_ARTICLE_LINK
        defaultArticleShouldNotBeFound("articleLink.contains=" + UPDATED_ARTICLE_LINK);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleLinkNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleLink does not contain DEFAULT_ARTICLE_LINK
        defaultArticleShouldNotBeFound("articleLink.doesNotContain=" + DEFAULT_ARTICLE_LINK);

        // Get all the articleList where articleLink does not contain UPDATED_ARTICLE_LINK
        defaultArticleShouldBeFound("articleLink.doesNotContain=" + UPDATED_ARTICLE_LINK);
    }


    @Test
    @Transactional
    public void getAllArticlesByArticleJournalIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal equals to DEFAULT_ARTICLE_JOURNAL
        defaultArticleShouldBeFound("articleJournal.equals=" + DEFAULT_ARTICLE_JOURNAL);

        // Get all the articleList where articleJournal equals to UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldNotBeFound("articleJournal.equals=" + UPDATED_ARTICLE_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleJournalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal not equals to DEFAULT_ARTICLE_JOURNAL
        defaultArticleShouldNotBeFound("articleJournal.notEquals=" + DEFAULT_ARTICLE_JOURNAL);

        // Get all the articleList where articleJournal not equals to UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldBeFound("articleJournal.notEquals=" + UPDATED_ARTICLE_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleJournalIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal in DEFAULT_ARTICLE_JOURNAL or UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldBeFound("articleJournal.in=" + DEFAULT_ARTICLE_JOURNAL + "," + UPDATED_ARTICLE_JOURNAL);

        // Get all the articleList where articleJournal equals to UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldNotBeFound("articleJournal.in=" + UPDATED_ARTICLE_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleJournalIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal is not null
        defaultArticleShouldBeFound("articleJournal.specified=true");

        // Get all the articleList where articleJournal is null
        defaultArticleShouldNotBeFound("articleJournal.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByArticleJournalContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal contains DEFAULT_ARTICLE_JOURNAL
        defaultArticleShouldBeFound("articleJournal.contains=" + DEFAULT_ARTICLE_JOURNAL);

        // Get all the articleList where articleJournal contains UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldNotBeFound("articleJournal.contains=" + UPDATED_ARTICLE_JOURNAL);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleJournalNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleJournal does not contain DEFAULT_ARTICLE_JOURNAL
        defaultArticleShouldNotBeFound("articleJournal.doesNotContain=" + DEFAULT_ARTICLE_JOURNAL);

        // Get all the articleList where articleJournal does not contain UPDATED_ARTICLE_JOURNAL
        defaultArticleShouldBeFound("articleJournal.doesNotContain=" + UPDATED_ARTICLE_JOURNAL);
    }


    @Test
    @Transactional
    public void getAllArticlesByArticleCitationIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation equals to DEFAULT_ARTICLE_CITATION
        defaultArticleShouldBeFound("articleCitation.equals=" + DEFAULT_ARTICLE_CITATION);

        // Get all the articleList where articleCitation equals to UPDATED_ARTICLE_CITATION
        defaultArticleShouldNotBeFound("articleCitation.equals=" + UPDATED_ARTICLE_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleCitationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation not equals to DEFAULT_ARTICLE_CITATION
        defaultArticleShouldNotBeFound("articleCitation.notEquals=" + DEFAULT_ARTICLE_CITATION);

        // Get all the articleList where articleCitation not equals to UPDATED_ARTICLE_CITATION
        defaultArticleShouldBeFound("articleCitation.notEquals=" + UPDATED_ARTICLE_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleCitationIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation in DEFAULT_ARTICLE_CITATION or UPDATED_ARTICLE_CITATION
        defaultArticleShouldBeFound("articleCitation.in=" + DEFAULT_ARTICLE_CITATION + "," + UPDATED_ARTICLE_CITATION);

        // Get all the articleList where articleCitation equals to UPDATED_ARTICLE_CITATION
        defaultArticleShouldNotBeFound("articleCitation.in=" + UPDATED_ARTICLE_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleCitationIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation is not null
        defaultArticleShouldBeFound("articleCitation.specified=true");

        // Get all the articleList where articleCitation is null
        defaultArticleShouldNotBeFound("articleCitation.specified=false");
    }
                @Test
    @Transactional
    public void getAllArticlesByArticleCitationContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation contains DEFAULT_ARTICLE_CITATION
        defaultArticleShouldBeFound("articleCitation.contains=" + DEFAULT_ARTICLE_CITATION);

        // Get all the articleList where articleCitation contains UPDATED_ARTICLE_CITATION
        defaultArticleShouldNotBeFound("articleCitation.contains=" + UPDATED_ARTICLE_CITATION);
    }

    @Test
    @Transactional
    public void getAllArticlesByArticleCitationNotContainsSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where articleCitation does not contain DEFAULT_ARTICLE_CITATION
        defaultArticleShouldNotBeFound("articleCitation.doesNotContain=" + DEFAULT_ARTICLE_CITATION);

        // Get all the articleList where articleCitation does not contain UPDATED_ARTICLE_CITATION
        defaultArticleShouldBeFound("articleCitation.doesNotContain=" + UPDATED_ARTICLE_CITATION);
    }


    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate equals to DEFAULT_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.equals=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate equals to UPDATED_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.equals=" + UPDATED_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate not equals to DEFAULT_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.notEquals=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate not equals to UPDATED_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.notEquals=" + UPDATED_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsInShouldWork() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate in DEFAULT_FETCH_DATE or UPDATED_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.in=" + DEFAULT_FETCH_DATE + "," + UPDATED_FETCH_DATE);

        // Get all the articleList where fetchDate equals to UPDATED_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.in=" + UPDATED_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate is not null
        defaultArticleShouldBeFound("fetchDate.specified=true");

        // Get all the articleList where fetchDate is null
        defaultArticleShouldNotBeFound("fetchDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate is greater than or equal to DEFAULT_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.greaterThanOrEqual=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate is greater than or equal to UPDATED_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.greaterThanOrEqual=" + UPDATED_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate is less than or equal to DEFAULT_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.lessThanOrEqual=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate is less than or equal to SMALLER_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.lessThanOrEqual=" + SMALLER_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsLessThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate is less than DEFAULT_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.lessThan=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate is less than UPDATED_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.lessThan=" + UPDATED_FETCH_DATE);
    }

    @Test
    @Transactional
    public void getAllArticlesByFetchDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);

        // Get all the articleList where fetchDate is greater than DEFAULT_FETCH_DATE
        defaultArticleShouldNotBeFound("fetchDate.greaterThan=" + DEFAULT_FETCH_DATE);

        // Get all the articleList where fetchDate is greater than SMALLER_FETCH_DATE
        defaultArticleShouldBeFound("fetchDate.greaterThan=" + SMALLER_FETCH_DATE);
    }


    @Test
    @Transactional
    public void getAllArticlesByRevisionIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        Revision revision = RevisionResourceIT.createEntity(em);
        em.persist(revision);
        em.flush();
        article.setRevision(revision);
        revision.setArticle(article);
        articleRepository.saveAndFlush(article);
        Long revisionId = revision.getId();

        // Get all the articleList where revision equals to revisionId
        defaultArticleShouldBeFound("revisionId.equals=" + revisionId);

        // Get all the articleList where revision equals to revisionId + 1
        defaultArticleShouldNotBeFound("revisionId.equals=" + (revisionId + 1));
    }


    @Test
    @Transactional
    public void getAllArticlesBySrepoIsEqualToSomething() throws Exception {
        // Initialize the database
        articleRepository.saveAndFlush(article);
        SourceRepo srepo = SourceRepoResourceIT.createEntity(em);
        em.persist(srepo);
        em.flush();
        article.setSrepo(srepo);
        articleRepository.saveAndFlush(article);
        Long srepoId = srepo.getId();

        // Get all the articleList where srepo equals to srepoId
        defaultArticleShouldBeFound("srepoId.equals=" + srepoId);

        // Get all the articleList where srepo equals to srepoId + 1
        defaultArticleShouldNotBeFound("srepoId.equals=" + (srepoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc.perform(get("/api/articles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].repoArticleId").value(hasItem(DEFAULT_REPO_ARTICLE_ID)))
            .andExpect(jsonPath("$.[*].repoDate").value(hasItem(DEFAULT_REPO_DATE.toString())))
            .andExpect(jsonPath("$.[*].repoKeywords").value(hasItem(DEFAULT_REPO_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].articleDate").value(hasItem(DEFAULT_ARTICLE_DATE)))
            .andExpect(jsonPath("$.[*].articleTitle").value(hasItem(DEFAULT_ARTICLE_TITLE)))
            .andExpect(jsonPath("$.[*].articleAbstract").value(hasItem(DEFAULT_ARTICLE_ABSTRACT.toString())))
            .andExpect(jsonPath("$.[*].articleLink").value(hasItem(DEFAULT_ARTICLE_LINK)))
            .andExpect(jsonPath("$.[*].articleJournal").value(hasItem(DEFAULT_ARTICLE_JOURNAL)))
            .andExpect(jsonPath("$.[*].articleCitation").value(hasItem(DEFAULT_ARTICLE_CITATION)))
            .andExpect(jsonPath("$.[*].fetchDate").value(hasItem(DEFAULT_FETCH_DATE.toString())));

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
            .repoArticleId(UPDATED_REPO_ARTICLE_ID)
            .repoDate(UPDATED_REPO_DATE)
            .repoKeywords(UPDATED_REPO_KEYWORDS)
            .articleDate(UPDATED_ARTICLE_DATE)
            .articleTitle(UPDATED_ARTICLE_TITLE)
            .articleAbstract(UPDATED_ARTICLE_ABSTRACT)
            .articleLink(UPDATED_ARTICLE_LINK)
            .articleJournal(UPDATED_ARTICLE_JOURNAL)
            .articleCitation(UPDATED_ARTICLE_CITATION)
            .fetchDate(UPDATED_FETCH_DATE);

        restArticleMockMvc.perform(put("/api/articles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedArticle)))
            .andExpect(status().isOk());

        // Validate the Article in the database
        List<Article> articleList = articleRepository.findAll();
        assertThat(articleList).hasSize(databaseSizeBeforeUpdate);
        Article testArticle = articleList.get(articleList.size() - 1);
        assertThat(testArticle.getRepoArticleId()).isEqualTo(UPDATED_REPO_ARTICLE_ID);
        assertThat(testArticle.getRepoDate()).isEqualTo(UPDATED_REPO_DATE);
        assertThat(testArticle.getRepoKeywords()).isEqualTo(UPDATED_REPO_KEYWORDS);
        assertThat(testArticle.getArticleDate()).isEqualTo(UPDATED_ARTICLE_DATE);
        assertThat(testArticle.getArticleTitle()).isEqualTo(UPDATED_ARTICLE_TITLE);
        assertThat(testArticle.getArticleAbstract()).isEqualTo(UPDATED_ARTICLE_ABSTRACT);
        assertThat(testArticle.getArticleLink()).isEqualTo(UPDATED_ARTICLE_LINK);
        assertThat(testArticle.getArticleJournal()).isEqualTo(UPDATED_ARTICLE_JOURNAL);
        assertThat(testArticle.getArticleCitation()).isEqualTo(UPDATED_ARTICLE_CITATION);
        assertThat(testArticle.getFetchDate()).isEqualTo(UPDATED_FETCH_DATE);
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
