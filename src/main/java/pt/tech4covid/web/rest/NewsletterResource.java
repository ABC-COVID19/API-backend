package pt.tech4covid.web.rest;

import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.service.NewsletterService;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;
import pt.tech4covid.service.dto.NewsletterCriteria;
import pt.tech4covid.service.NewsletterQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.tech4covid.domain.Newsletter}.
 */
@RestController
@RequestMapping("/api")
public class NewsletterResource {

    private final Logger log = LoggerFactory.getLogger(NewsletterResource.class);

    private static final String ENTITY_NAME = "icamApiNewsletter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NewsletterService newsletterService;

    private final NewsletterQueryService newsletterQueryService;

    public NewsletterResource(NewsletterService newsletterService, NewsletterQueryService newsletterQueryService) {
        this.newsletterService = newsletterService;
        this.newsletterQueryService = newsletterQueryService;
    }

    /**
     * {@code POST  /newsletters} : Create a new newsletter.
     *
     * @param newsletter the newsletter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new newsletter, or with status {@code 400 (Bad Request)} if the newsletter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/newsletters")
    public ResponseEntity<Newsletter> createNewsletter(@Valid @RequestBody Newsletter newsletter) throws URISyntaxException {
        log.debug("REST request to save Newsletter : {}", newsletter);
        if (newsletter.getId() != null) {
            throw new BadRequestAlertException("A new newsletter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Newsletter result = newsletterService.save(newsletter);
        return ResponseEntity.created(new URI("/api/newsletters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /newsletters} : Updates an existing newsletter.
     *
     * @param newsletter the newsletter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsletter,
     * or with status {@code 400 (Bad Request)} if the newsletter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsletter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/newsletters")
    public ResponseEntity<Newsletter> updateNewsletter(@Valid @RequestBody Newsletter newsletter) throws URISyntaxException {
        log.debug("REST request to update Newsletter : {}", newsletter);
        if (newsletter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Newsletter result = newsletterService.save(newsletter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, newsletter.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /newsletters} : get all the newsletters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of newsletters in body.
     */
    @GetMapping("/newsletters")
    public ResponseEntity<List<Newsletter>> getAllNewsletters(NewsletterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Newsletters by criteria: {}", criteria);
        Page<Newsletter> page = newsletterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /newsletters/count} : count all the newsletters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/newsletters/count")
    public ResponseEntity<Long> countNewsletters(NewsletterCriteria criteria) {
        log.debug("REST request to count Newsletters by criteria: {}", criteria);
        return ResponseEntity.ok().body(newsletterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /newsletters/:id} : get the "id" newsletter.
     *
     * @param id the id of the newsletter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the newsletter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/newsletters/{id}")
    public ResponseEntity<Newsletter> getNewsletter(@PathVariable Long id) {
        log.debug("REST request to get Newsletter : {}", id);
        Optional<Newsletter> newsletter = newsletterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsletter);
    }

    /**
     * {@code DELETE  /newsletters/:id} : delete the "id" newsletter.
     *
     * @param id the id of the newsletter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/newsletters/{id}")
    public ResponseEntity<Void> deleteNewsletter(@PathVariable Long id) {
        log.debug("REST request to delete Newsletter : {}", id);
        newsletterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
