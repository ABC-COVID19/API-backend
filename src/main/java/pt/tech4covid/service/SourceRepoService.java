package pt.tech4covid.service;

import pt.tech4covid.domain.SourceRepo;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SourceRepo}.
 */
public interface SourceRepoService {

    /**
     * Save a sourceRepo.
     *
     * @param sourceRepo the entity to save.
     * @return the persisted entity.
     */
    SourceRepo save(SourceRepo sourceRepo);

    /**
     * Get all the sourceRepos.
     *
     * @return the list of entities.
     */
    List<SourceRepo> findAll();

    /**
     * Get the "id" sourceRepo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SourceRepo> findOne(Long id);

    /**
     * Delete the "id" sourceRepo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
