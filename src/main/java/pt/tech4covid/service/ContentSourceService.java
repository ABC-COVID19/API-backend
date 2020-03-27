package pt.tech4covid.service;

import pt.tech4covid.domain.ContentSource;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ContentSource}.
 */
public interface ContentSourceService {

    /**
     * Save a contentSource.
     *
     * @param contentSource the entity to save.
     * @return the persisted entity.
     */
    ContentSource save(ContentSource contentSource);

    /**
     * Get all the contentSources.
     *
     * @return the list of entities.
     */
    List<ContentSource> findAll();

    /**
     * Get the "id" contentSource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContentSource> findOne(Long id);

    /**
     * Delete the "id" contentSource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
