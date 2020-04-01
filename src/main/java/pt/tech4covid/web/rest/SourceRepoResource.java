package pt.tech4covid.web.rest;

import pt.tech4covid.domain.SourceRepo;
import pt.tech4covid.service.SourceRepoService;
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
 * REST controller for managing {@link pt.tech4covid.domain.SourceRepo}.
 */
@RestController
@RequestMapping("/api")
public class SourceRepoResource {

    private final Logger log = LoggerFactory.getLogger(SourceRepoResource.class);

    private static final String ENTITY_NAME = "icamApiSourceRepo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceRepoService sourceRepoService;

    public SourceRepoResource(SourceRepoService sourceRepoService) {
        this.sourceRepoService = sourceRepoService;
    }

    /**
     * {@code POST  /source-repos} : Create a new sourceRepo.
     *
     * @param sourceRepo the sourceRepo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceRepo, or with status {@code 400 (Bad Request)} if the sourceRepo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/source-repos")
    public ResponseEntity<SourceRepo> createSourceRepo(@Valid @RequestBody SourceRepo sourceRepo) throws URISyntaxException {
        log.debug("REST request to save SourceRepo : {}", sourceRepo);
        if (sourceRepo.getId() != null) {
            throw new BadRequestAlertException("A new sourceRepo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceRepo result = sourceRepoService.save(sourceRepo);
        return ResponseEntity.created(new URI("/api/source-repos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-repos} : Updates an existing sourceRepo.
     *
     * @param sourceRepo the sourceRepo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceRepo,
     * or with status {@code 400 (Bad Request)} if the sourceRepo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceRepo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-repos")
    public ResponseEntity<SourceRepo> updateSourceRepo(@Valid @RequestBody SourceRepo sourceRepo) throws URISyntaxException {
        log.debug("REST request to update SourceRepo : {}", sourceRepo);
        if (sourceRepo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SourceRepo result = sourceRepoService.save(sourceRepo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sourceRepo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /source-repos} : get all the sourceRepos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceRepos in body.
     */
    @GetMapping("/source-repos")
    public List<SourceRepo> getAllSourceRepos() {
        log.debug("REST request to get all SourceRepos");
        return sourceRepoService.findAll();
    }

    /**
     * {@code GET  /source-repos/:id} : get the "id" sourceRepo.
     *
     * @param id the id of the sourceRepo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceRepo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/source-repos/{id}")
    public ResponseEntity<SourceRepo> getSourceRepo(@PathVariable Long id) {
        log.debug("REST request to get SourceRepo : {}", id);
        Optional<SourceRepo> sourceRepo = sourceRepoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceRepo);
    }

    /**
     * {@code DELETE  /source-repos/:id} : delete the "id" sourceRepo.
     *
     * @param id the id of the sourceRepo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/source-repos/{id}")
    public ResponseEntity<Void> deleteSourceRepo(@PathVariable Long id) {
        log.debug("REST request to delete SourceRepo : {}", id);
        sourceRepoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
