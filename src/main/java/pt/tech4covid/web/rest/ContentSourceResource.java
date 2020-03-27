package pt.tech4covid.web.rest;

import pt.tech4covid.domain.ContentSource;
import pt.tech4covid.service.ContentSourceService;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.tech4covid.domain.ContentSource}.
 */
@RestController
@RequestMapping("/api")
public class ContentSourceResource {

    private final Logger log = LoggerFactory.getLogger(ContentSourceResource.class);

    private static final String ENTITY_NAME = "icamApiContentSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentSourceService contentSourceService;

    public ContentSourceResource(ContentSourceService contentSourceService) {
        this.contentSourceService = contentSourceService;
    }

    /**
     * {@code POST  /content-sources} : Create a new contentSource.
     *
     * @param contentSource the contentSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentSource, or with status {@code 400 (Bad Request)} if the contentSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/content-sources")
    public ResponseEntity<ContentSource> createContentSource(@Valid @RequestBody ContentSource contentSource) throws URISyntaxException {
        log.debug("REST request to save ContentSource : {}", contentSource);
        if (contentSource.getId() != null) {
            throw new BadRequestAlertException("A new contentSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentSource result = contentSourceService.save(contentSource);
        return ResponseEntity.created(new URI("/api/content-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-sources} : Updates an existing contentSource.
     *
     * @param contentSource the contentSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentSource,
     * or with status {@code 400 (Bad Request)} if the contentSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/content-sources")
    public ResponseEntity<ContentSource> updateContentSource(@Valid @RequestBody ContentSource contentSource) throws URISyntaxException {
        log.debug("REST request to update ContentSource : {}", contentSource);
        if (contentSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContentSource result = contentSourceService.save(contentSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contentSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /content-sources} : get all the contentSources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentSources in body.
     */
    @GetMapping("/content-sources")
    public List<ContentSource> getAllContentSources() {
        log.debug("REST request to get all ContentSources");
        return contentSourceService.findAll();
    }

    /**
     * {@code GET  /content-sources/:id} : get the "id" contentSource.
     *
     * @param id the id of the contentSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-sources/{id}")
    public ResponseEntity<ContentSource> getContentSource(@PathVariable Long id) {
        log.debug("REST request to get ContentSource : {}", id);
        Optional<ContentSource> contentSource = contentSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentSource);
    }

    /**
     * {@code DELETE  /content-sources/:id} : delete the "id" contentSource.
     *
     * @param id the id of the contentSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/content-sources/{id}")
    public ResponseEntity<Void> deleteContentSource(@PathVariable Long id) {
        log.debug("REST request to delete ContentSource : {}", id);
        contentSourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
