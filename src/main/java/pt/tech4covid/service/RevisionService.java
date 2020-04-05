package pt.tech4covid.service;

import pt.tech4covid.domain.Revision;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Revision}.
 */
public interface RevisionService {

    /**
     * Save a revision.
     *
     * @param revision the entity to save.
     * @return the persisted entity.
     */
    Revision save(Revision revision);

    /**
     * Get all the revisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Revision> findAll(Pageable pageable);

    /**
     * Get all the revisions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Revision> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" revision.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Revision> findOne(Long id);

    /**
     * Delete the "id" revision.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
