package pt.tech4covid.web.rest;

import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.service.CategoryTreeService;
import pt.tech4covid.web.rest.errors.BadRequestAlertException;
import pt.tech4covid.service.dto.CategoryTreeCriteria;
import pt.tech4covid.service.CategoryTreeQueryService;

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
 * REST controller for managing {@link pt.tech4covid.domain.CategoryTree}.
 */
@RestController
@RequestMapping("/api")
public class CategoryTreeResource {

    private final Logger log = LoggerFactory.getLogger(CategoryTreeResource.class);

    private static final String ENTITY_NAME = "icamApiCategoryTree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryTreeService categoryTreeService;

    private final CategoryTreeQueryService categoryTreeQueryService;

    public CategoryTreeResource(CategoryTreeService categoryTreeService, CategoryTreeQueryService categoryTreeQueryService) {
        this.categoryTreeService = categoryTreeService;
        this.categoryTreeQueryService = categoryTreeQueryService;
    }

    /**
     * {@code POST  /category-trees} : Create a new categoryTree.
     *
     * @param categoryTree the categoryTree to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryTree, or with status {@code 400 (Bad Request)} if the categoryTree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/category-trees")
    public ResponseEntity<CategoryTree> createCategoryTree(@Valid @RequestBody CategoryTree categoryTree) throws URISyntaxException {
        log.debug("REST request to save CategoryTree : {}", categoryTree);
        if (categoryTree.getId() != null) {
            throw new BadRequestAlertException("A new categoryTree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryTree result = categoryTreeService.save(categoryTree);
        return ResponseEntity.created(new URI("/api/category-trees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /category-trees} : Updates an existing categoryTree.
     *
     * @param categoryTree the categoryTree to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryTree,
     * or with status {@code 400 (Bad Request)} if the categoryTree is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryTree couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/category-trees")
    public ResponseEntity<CategoryTree> updateCategoryTree(@Valid @RequestBody CategoryTree categoryTree) throws URISyntaxException {
        log.debug("REST request to update CategoryTree : {}", categoryTree);
        if (categoryTree.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategoryTree result = categoryTreeService.save(categoryTree);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categoryTree.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /category-trees} : get all the categoryTrees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryTrees in body.
     */
    @GetMapping("/category-trees")
    public ResponseEntity<List<CategoryTree>> getAllCategoryTrees(CategoryTreeCriteria criteria) {
        log.debug("REST request to get CategoryTrees by criteria: {}", criteria);
        List<CategoryTree> entityList = categoryTreeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /category-trees/count} : count all the categoryTrees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/category-trees/count")
    public ResponseEntity<Long> countCategoryTrees(CategoryTreeCriteria criteria) {
        log.debug("REST request to count CategoryTrees by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryTreeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-trees/:id} : get the "id" categoryTree.
     *
     * @param id the id of the categoryTree to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryTree, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/category-trees/{id}")
    public ResponseEntity<CategoryTree> getCategoryTree(@PathVariable Long id) {
        log.debug("REST request to get CategoryTree : {}", id);
        Optional<CategoryTree> categoryTree = categoryTreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryTree);
    }

    /**
     * {@code DELETE  /category-trees/:id} : delete the "id" categoryTree.
     *
     * @param id the id of the categoryTree to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/category-trees/{id}")
    public ResponseEntity<Void> deleteCategoryTree(@PathVariable Long id) {
        log.debug("REST request to delete CategoryTree : {}", id);
        categoryTreeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
