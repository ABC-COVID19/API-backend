package pt.tech4covid.web.rest;

import pt.tech4covid.domain.Revision;
import pt.tech4covid.repository.RevisionRepository;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.tech4covid.domain.Revision}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RevisionResource {

    private final Logger log = LoggerFactory.getLogger(RevisionResource.class);

    private static final String ENTITY_NAME = "revision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RevisionRepository revisionRepository;

    public RevisionResource(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    /**
     * {@code POST  /revisions} : Create a new revision.
     *
     * @param revision the revision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new revision, or with status {@code 400 (Bad Request)} if the revision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/revisions")
    public ResponseEntity<Revision> createRevision(@RequestBody Revision revision) throws URISyntaxException {
        log.debug("REST request to save Revision : {}", revision);
        if (revision.getId() != null) {
            throw new BadRequestAlertException("A new revision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Revision result = revisionRepository.save(revision);
        return ResponseEntity.created(new URI("/api/revisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /revisions} : Updates an existing revision.
     *
     * @param revision the revision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated revision,
     * or with status {@code 400 (Bad Request)} if the revision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the revision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/revisions")
    public ResponseEntity<Revision> updateRevision(@RequestBody Revision revision) throws URISyntaxException {
        log.debug("REST request to update Revision : {}", revision);
        if (revision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Revision result = revisionRepository.save(revision);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, revision.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /revisions} : get all the revisions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of revisions in body.
     */
    @GetMapping("/revisions")
    public ResponseEntity<List<Revision>> getAllRevisions(Pageable pageable) {
        log.debug("REST request to get a page of Revisions");
        Page<Revision> page = revisionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /revisions/:id} : get the "id" revision.
     *
     * @param id the id of the revision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the revision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/revisions/{id}")
    public ResponseEntity<Revision> getRevision(@PathVariable Long id) {
        log.debug("REST request to get Revision : {}", id);
        Optional<Revision> revision = revisionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(revision);
    }

    /**
     * {@code DELETE  /revisions/:id} : delete the "id" revision.
     *
     * @param id the id of the revision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/revisions/{id}")
    public ResponseEntity<Void> deleteRevision(@PathVariable Long id) {
        log.debug("REST request to delete Revision : {}", id);
        revisionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
