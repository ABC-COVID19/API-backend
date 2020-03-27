package pt.tech4covid.web.rest;

import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.service.ArticleTypeService;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;
import pt.tech4covid.service.dto.ArticleTypeCriteria;
import pt.tech4covid.service.ArticleTypeQueryService;

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
 * REST controller for managing {@link pt.tech4covid.domain.ArticleType}.
 */
@RestController
@RequestMapping("/api")
public class ArticleTypeResource {

    private final Logger log = LoggerFactory.getLogger(ArticleTypeResource.class);

    private static final String ENTITY_NAME = "icamApiArticleType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArticleTypeService articleTypeService;

    private final ArticleTypeQueryService articleTypeQueryService;

    public ArticleTypeResource(ArticleTypeService articleTypeService, ArticleTypeQueryService articleTypeQueryService) {
        this.articleTypeService = articleTypeService;
        this.articleTypeQueryService = articleTypeQueryService;
    }

    /**
     * {@code POST  /article-types} : Create a new articleType.
     *
     * @param articleType the articleType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new articleType, or with status {@code 400 (Bad Request)} if the articleType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/article-types")
    public ResponseEntity<ArticleType> createArticleType(@Valid @RequestBody ArticleType articleType) throws URISyntaxException {
        log.debug("REST request to save ArticleType : {}", articleType);
        if (articleType.getId() != null) {
            throw new BadRequestAlertException("A new articleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArticleType result = articleTypeService.save(articleType);
        return ResponseEntity.created(new URI("/api/article-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /article-types} : Updates an existing articleType.
     *
     * @param articleType the articleType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated articleType,
     * or with status {@code 400 (Bad Request)} if the articleType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the articleType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/article-types")
    public ResponseEntity<ArticleType> updateArticleType(@Valid @RequestBody ArticleType articleType) throws URISyntaxException {
        log.debug("REST request to update ArticleType : {}", articleType);
        if (articleType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArticleType result = articleTypeService.save(articleType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, articleType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /article-types} : get all the articleTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articleTypes in body.
     */
    @GetMapping("/article-types")
    public ResponseEntity<List<ArticleType>> getAllArticleTypes(ArticleTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ArticleTypes by criteria: {}", criteria);
        Page<ArticleType> page = articleTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /article-types/count} : count all the articleTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/article-types/count")
    public ResponseEntity<Long> countArticleTypes(ArticleTypeCriteria criteria) {
        log.debug("REST request to count ArticleTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(articleTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /article-types/:id} : get the "id" articleType.
     *
     * @param id the id of the articleType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/article-types/{id}")
    public ResponseEntity<ArticleType> getArticleType(@PathVariable Long id) {
        log.debug("REST request to get ArticleType : {}", id);
        Optional<ArticleType> articleType = articleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(articleType);
    }

    /**
     * {@code DELETE  /article-types/:id} : delete the "id" articleType.
     *
     * @param id the id of the articleType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/article-types/{id}")
    public ResponseEntity<Void> deleteArticleType(@PathVariable Long id) {
        log.debug("REST request to delete ArticleType : {}", id);
        articleTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
