package pt.tech4covid.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.tech4covid.domain.Newsletter;

import java.util.Optional;

/**
 * Service Interface for managing {@link Newsletter}.
 */
public interface NewsletterService {

    /**
     * Save a newsletter.
     *
     * @param newsletter the entity to save.
     * @return the persisted entity.
     */
    Newsletter save(Newsletter newsletter);

    /**
     * Get all the newsletters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Newsletter> findAll(Pageable pageable);

    /**
     * Get all the newsletters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Newsletter> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" newsletter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Newsletter> findOne(Long id);

    /**
     * Delete the "id" newsletter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get number of entries in repository.
     */
    long count();
}
