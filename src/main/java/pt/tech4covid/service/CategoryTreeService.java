package pt.tech4covid.service;

import pt.tech4covid.domain.CategoryTree;

import java.util.List;
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
     * @return the list of entities.
     */
    List<CategoryTree> findAll();

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
