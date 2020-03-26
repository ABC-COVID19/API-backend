package pt.tech4covid.web.rest;

import pt.tech4covid.IcamApiApp;
import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.repository.NewsletterRepository;
import pt.tech4covid.service.NewsletterService;
import pt.tech4covid.service.dto.NewsletterCriteria;
import pt.tech4covid.service.NewsletterQueryService;

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

/**
 * Integration tests for the {@link NewsletterResource} REST controller.
 */
@SpringBootTest(classes = IcamApiApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class NewsletterResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_RGPD_AUTH = false;
    private static final Boolean UPDATED_RGPD_AUTH = true;

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Mock
    private NewsletterRepository newsletterRepositoryMock;

    @Mock
    private NewsletterService newsletterServiceMock;

    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private NewsletterQueryService newsletterQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNewsletterMockMvc;

    private Newsletter newsletter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Newsletter createEntity(EntityManager em) {
        Newsletter newsletter = new Newsletter()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .rgpdAuth(DEFAULT_RGPD_AUTH);
        return newsletter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Newsletter createUpdatedEntity(EntityManager em) {
        Newsletter newsletter = new Newsletter()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .rgpdAuth(UPDATED_RGPD_AUTH);
        return newsletter;
    }

    @BeforeEach
    public void initTest() {
        newsletter = createEntity(em);
    }

    @Test
    @Transactional
    public void createNewsletter() throws Exception {
        int databaseSizeBeforeCreate = newsletterRepository.findAll().size();

        // Create the Newsletter
        restNewsletterMockMvc.perform(post("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isCreated());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeCreate + 1);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testNewsletter.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testNewsletter.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testNewsletter.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
        assertThat(testNewsletter.isRgpdAuth()).isEqualTo(DEFAULT_RGPD_AUTH);
    }

    @Test
    @Transactional
    public void createNewsletterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = newsletterRepository.findAll().size();

        // Create the Newsletter with an existing ID
        newsletter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNewsletterMockMvc.perform(post("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsletterRepository.findAll().size();
        // set the field null
        newsletter.setEmail(null);

        // Create the Newsletter, which fails.

        restNewsletterMockMvc.perform(post("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isBadRequest());

        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistrationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsletterRepository.findAll().size();
        // set the field null
        newsletter.setRegistrationDate(null);

        // Create the Newsletter, which fails.

        restNewsletterMockMvc.perform(post("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isBadRequest());

        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRgpdAuthIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsletterRepository.findAll().size();
        // set the field null
        newsletter.setRgpdAuth(null);

        // Create the Newsletter, which fails.

        restNewsletterMockMvc.perform(post("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isBadRequest());

        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNewsletters() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList
        restNewsletterMockMvc.perform(get("/api/newsletters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsletter.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].rgpdAuth").value(hasItem(DEFAULT_RGPD_AUTH.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllNewslettersWithEagerRelationshipsIsEnabled() throws Exception {
        when(newsletterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNewsletterMockMvc.perform(get("/api/newsletters?eagerload=true"))
            .andExpect(status().isOk());

        verify(newsletterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllNewslettersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(newsletterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNewsletterMockMvc.perform(get("/api/newsletters?eagerload=true"))
            .andExpect(status().isOk());

        verify(newsletterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getNewsletter() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get the newsletter
        restNewsletterMockMvc.perform(get("/api/newsletters/{id}", newsletter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(newsletter.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.rgpdAuth").value(DEFAULT_RGPD_AUTH.booleanValue()));
    }


    @Test
    @Transactional
    public void getNewslettersByIdFiltering() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        Long id = newsletter.getId();

        defaultNewsletterShouldBeFound("id.equals=" + id);
        defaultNewsletterShouldNotBeFound("id.notEquals=" + id);

        defaultNewsletterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNewsletterShouldNotBeFound("id.greaterThan=" + id);

        defaultNewsletterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNewsletterShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNewslettersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName equals to DEFAULT_FIRST_NAME
        defaultNewsletterShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the newsletterList where firstName equals to UPDATED_FIRST_NAME
        defaultNewsletterShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName not equals to DEFAULT_FIRST_NAME
        defaultNewsletterShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the newsletterList where firstName not equals to UPDATED_FIRST_NAME
        defaultNewsletterShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultNewsletterShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the newsletterList where firstName equals to UPDATED_FIRST_NAME
        defaultNewsletterShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName is not null
        defaultNewsletterShouldBeFound("firstName.specified=true");

        // Get all the newsletterList where firstName is null
        defaultNewsletterShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllNewslettersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName contains DEFAULT_FIRST_NAME
        defaultNewsletterShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the newsletterList where firstName contains UPDATED_FIRST_NAME
        defaultNewsletterShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where firstName does not contain DEFAULT_FIRST_NAME
        defaultNewsletterShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the newsletterList where firstName does not contain UPDATED_FIRST_NAME
        defaultNewsletterShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllNewslettersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName equals to DEFAULT_LAST_NAME
        defaultNewsletterShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the newsletterList where lastName equals to UPDATED_LAST_NAME
        defaultNewsletterShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName not equals to DEFAULT_LAST_NAME
        defaultNewsletterShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the newsletterList where lastName not equals to UPDATED_LAST_NAME
        defaultNewsletterShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultNewsletterShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the newsletterList where lastName equals to UPDATED_LAST_NAME
        defaultNewsletterShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName is not null
        defaultNewsletterShouldBeFound("lastName.specified=true");

        // Get all the newsletterList where lastName is null
        defaultNewsletterShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllNewslettersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName contains DEFAULT_LAST_NAME
        defaultNewsletterShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the newsletterList where lastName contains UPDATED_LAST_NAME
        defaultNewsletterShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllNewslettersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where lastName does not contain DEFAULT_LAST_NAME
        defaultNewsletterShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the newsletterList where lastName does not contain UPDATED_LAST_NAME
        defaultNewsletterShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllNewslettersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email equals to DEFAULT_EMAIL
        defaultNewsletterShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email equals to UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNewslettersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email not equals to DEFAULT_EMAIL
        defaultNewsletterShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email not equals to UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNewslettersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the newsletterList where email equals to UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNewslettersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email is not null
        defaultNewsletterShouldBeFound("email.specified=true");

        // Get all the newsletterList where email is null
        defaultNewsletterShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllNewslettersByEmailContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email contains DEFAULT_EMAIL
        defaultNewsletterShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email contains UPDATED_EMAIL
        defaultNewsletterShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllNewslettersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where email does not contain DEFAULT_EMAIL
        defaultNewsletterShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the newsletterList where email does not contain UPDATED_EMAIL
        defaultNewsletterShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate is not null
        defaultNewsletterShouldBeFound("registrationDate.specified=true");

        // Get all the newsletterList where registrationDate is null
        defaultNewsletterShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultNewsletterShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the newsletterList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultNewsletterShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllNewslettersByRgpdAuthIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where rgpdAuth equals to DEFAULT_RGPD_AUTH
        defaultNewsletterShouldBeFound("rgpdAuth.equals=" + DEFAULT_RGPD_AUTH);

        // Get all the newsletterList where rgpdAuth equals to UPDATED_RGPD_AUTH
        defaultNewsletterShouldNotBeFound("rgpdAuth.equals=" + UPDATED_RGPD_AUTH);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRgpdAuthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where rgpdAuth not equals to DEFAULT_RGPD_AUTH
        defaultNewsletterShouldNotBeFound("rgpdAuth.notEquals=" + DEFAULT_RGPD_AUTH);

        // Get all the newsletterList where rgpdAuth not equals to UPDATED_RGPD_AUTH
        defaultNewsletterShouldBeFound("rgpdAuth.notEquals=" + UPDATED_RGPD_AUTH);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRgpdAuthIsInShouldWork() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where rgpdAuth in DEFAULT_RGPD_AUTH or UPDATED_RGPD_AUTH
        defaultNewsletterShouldBeFound("rgpdAuth.in=" + DEFAULT_RGPD_AUTH + "," + UPDATED_RGPD_AUTH);

        // Get all the newsletterList where rgpdAuth equals to UPDATED_RGPD_AUTH
        defaultNewsletterShouldNotBeFound("rgpdAuth.in=" + UPDATED_RGPD_AUTH);
    }

    @Test
    @Transactional
    public void getAllNewslettersByRgpdAuthIsNullOrNotNull() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);

        // Get all the newsletterList where rgpdAuth is not null
        defaultNewsletterShouldBeFound("rgpdAuth.specified=true");

        // Get all the newsletterList where rgpdAuth is null
        defaultNewsletterShouldNotBeFound("rgpdAuth.specified=false");
    }

    @Test
    @Transactional
    public void getAllNewslettersByCategoryTreeIsEqualToSomething() throws Exception {
        // Initialize the database
        newsletterRepository.saveAndFlush(newsletter);
        CategoryTree categoryTree = CategoryTreeResourceIT.createEntity(em);
        em.persist(categoryTree);
        em.flush();
        newsletter.addCategoryTree(categoryTree);
        newsletterRepository.saveAndFlush(newsletter);
        Long categoryTreeId = categoryTree.getId();

        // Get all the newsletterList where categoryTree equals to categoryTreeId
        defaultNewsletterShouldBeFound("categoryTreeId.equals=" + categoryTreeId);

        // Get all the newsletterList where categoryTree equals to categoryTreeId + 1
        defaultNewsletterShouldNotBeFound("categoryTreeId.equals=" + (categoryTreeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNewsletterShouldBeFound(String filter) throws Exception {
        restNewsletterMockMvc.perform(get("/api/newsletters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(newsletter.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].rgpdAuth").value(hasItem(DEFAULT_RGPD_AUTH.booleanValue())));

        // Check, that the count call also returns 1
        restNewsletterMockMvc.perform(get("/api/newsletters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNewsletterShouldNotBeFound(String filter) throws Exception {
        restNewsletterMockMvc.perform(get("/api/newsletters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNewsletterMockMvc.perform(get("/api/newsletters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNewsletter() throws Exception {
        // Get the newsletter
        restNewsletterMockMvc.perform(get("/api/newsletters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNewsletter() throws Exception {
        // Initialize the database
        newsletterService.save(newsletter);

        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();

        // Update the newsletter
        Newsletter updatedNewsletter = newsletterRepository.findById(newsletter.getId()).get();
        // Disconnect from session so that the updates on updatedNewsletter are not directly saved in db
        em.detach(updatedNewsletter);
        updatedNewsletter
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .rgpdAuth(UPDATED_RGPD_AUTH);

        restNewsletterMockMvc.perform(put("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNewsletter)))
            .andExpect(status().isOk());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);
        Newsletter testNewsletter = newsletterList.get(newsletterList.size() - 1);
        assertThat(testNewsletter.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testNewsletter.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testNewsletter.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testNewsletter.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
        assertThat(testNewsletter.isRgpdAuth()).isEqualTo(UPDATED_RGPD_AUTH);
    }

    @Test
    @Transactional
    public void updateNonExistingNewsletter() throws Exception {
        int databaseSizeBeforeUpdate = newsletterRepository.findAll().size();

        // Create the Newsletter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNewsletterMockMvc.perform(put("/api/newsletters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(newsletter)))
            .andExpect(status().isBadRequest());

        // Validate the Newsletter in the database
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNewsletter() throws Exception {
        // Initialize the database
        newsletterService.save(newsletter);

        int databaseSizeBeforeDelete = newsletterRepository.findAll().size();

        // Delete the newsletter
        restNewsletterMockMvc.perform(delete("/api/newsletters/{id}", newsletter.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Newsletter> newsletterList = newsletterRepository.findAll();
        assertThat(newsletterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
