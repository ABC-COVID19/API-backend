package pt.tech4covid.web.rest;

import pt.tech4covid.domain.PublicationSource;
import pt.tech4covid.service.PublicationSourceService;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;
import pt.tech4covid.service.dto.PublicationSourceCriteria;
import pt.tech4covid.service.PublicationSourceQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.tech4covid.domain.PublicationSource}.
 */
@RestController
@RequestMapping("/api")
public class PublicationSourceResource {

    private final Logger log = LoggerFactory.getLogger(PublicationSourceResource.class);

    private static final String ENTITY_NAME = "icamApiPublicationSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublicationSourceService publicationSourceService;

    private final PublicationSourceQueryService publicationSourceQueryService;

    public PublicationSourceResource(PublicationSourceService publicationSourceService, PublicationSourceQueryService publicationSourceQueryService) {
        this.publicationSourceService = publicationSourceService;
        this.publicationSourceQueryService = publicationSourceQueryService;
    }

    /**
     * {@code POST  /publication-sources} : Create a new publicationSource.
     *
     * @param publicationSource the publicationSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publicationSource, or with status {@code 400 (Bad Request)} if the publicationSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/publication-sources")
    public ResponseEntity<PublicationSource> createPublicationSource(@RequestBody PublicationSource publicationSource) throws URISyntaxException {
        log.debug("REST request to save PublicationSource : {}", publicationSource);
        if (publicationSource.getId() != null) {
            throw new BadRequestAlertException("A new publicationSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublicationSource result = publicationSourceService.save(publicationSource);
        return ResponseEntity.created(new URI("/api/publication-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /publication-sources} : Updates an existing publicationSource.
     *
     * @param publicationSource the publicationSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publicationSource,
     * or with status {@code 400 (Bad Request)} if the publicationSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publicationSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/publication-sources")
    public ResponseEntity<PublicationSource> updatePublicationSource(@RequestBody PublicationSource publicationSource) throws URISyntaxException {
        log.debug("REST request to update PublicationSource : {}", publicationSource);
        if (publicationSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PublicationSource result = publicationSourceService.save(publicationSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publicationSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /publication-sources} : get all the publicationSources.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publicationSources in body.
     */
    @GetMapping("/publication-sources")
    public ResponseEntity<List<PublicationSource>> getAllPublicationSources(PublicationSourceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PublicationSources by criteria: {}", criteria);
        Page<PublicationSource> page = publicationSourceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /publication-sources/count} : count all the publicationSources.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/publication-sources/count")
    public ResponseEntity<Long> countPublicationSources(PublicationSourceCriteria criteria) {
        log.debug("REST request to count PublicationSources by criteria: {}", criteria);
        return ResponseEntity.ok().body(publicationSourceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /publication-sources/:id} : get the "id" publicationSource.
     *
     * @param id the id of the publicationSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publicationSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/publication-sources/{id}")
    public ResponseEntity<PublicationSource> getPublicationSource(@PathVariable Long id) {
        log.debug("REST request to get PublicationSource : {}", id);
        Optional<PublicationSource> publicationSource = publicationSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicationSource);
    }

    /**
     * {@code DELETE  /publication-sources/:id} : delete the "id" publicationSource.
     *
     * @param id the id of the publicationSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/publication-sources/{id}")
    public ResponseEntity<Void> deletePublicationSource(@PathVariable Long id) {
        log.debug("REST request to delete PublicationSource : {}", id);
        publicationSourceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
