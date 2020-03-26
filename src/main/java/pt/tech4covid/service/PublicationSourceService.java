package pt.tech4covid.service;

import pt.tech4covid.domain.PublicationSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link PublicationSource}.
 */
public interface PublicationSourceService {

    /**
     * Save a publicationSource.
     *
     * @param publicationSource the entity to save.
     * @return the persisted entity.
     */
    PublicationSource save(PublicationSource publicationSource);

    /**
     * Get all the publicationSources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PublicationSource> findAll(Pageable pageable);

    /**
     * Get the "id" publicationSource.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PublicationSource> findOne(Long id);

    /**
     * Delete the "id" publicationSource.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
