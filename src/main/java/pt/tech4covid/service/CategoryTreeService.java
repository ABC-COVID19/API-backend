package pt.tech4covid.service;

import pt.tech4covid.domain.CategoryTree;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CategoryTree}.
 */
public interface CategoryTreeService {

    /**
     * Save a categoryTree.
     *
     * @param categoryTree the entity to save.
     * @return the persisted entity.
     */
    CategoryTree save(CategoryTree categoryTree);

    /**
     * Get all the categoryTrees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategoryTree> findAll(Pageable pageable);

    /**
     * Get the "id" categoryTree.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryTree> findOne(Long id);

    /**
     * Delete the "id" categoryTree.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
