package pt.tech4covid.service.impl;

import pt.tech4covid.service.SourceRepoService;
import pt.tech4covid.domain.SourceRepo;
import pt.tech4covid.repository.SourceRepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link SourceRepo}.
 */
@Service
@Transactional
public class SourceRepoServiceImpl implements SourceRepoService {

    private final Logger log = LoggerFactory.getLogger(SourceRepoServiceImpl.class);

    private final SourceRepoRepository sourceRepoRepository;

    public SourceRepoServiceImpl(SourceRepoRepository sourceRepoRepository) {
        this.sourceRepoRepository = sourceRepoRepository;
    }

    /**
     * Save a sourceRepo.
     *
     * @param sourceRepo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SourceRepo save(SourceRepo sourceRepo) {
        log.debug("Request to save SourceRepo : {}", sourceRepo);
        return sourceRepoRepository.save(sourceRepo);
    }

    /**
     * Get all the sourceRepos.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SourceRepo> findAll() {
        log.debug("Request to get all SourceRepos");
        return sourceRepoRepository.findAll();
    }

    /**
     * Get one sourceRepo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SourceRepo> findOne(Long id) {
        log.debug("Request to get SourceRepo : {}", id);
        return sourceRepoRepository.findById(id);
    }

    /**
     * Delete the sourceRepo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SourceRepo : {}", id);
        sourceRepoRepository.deleteById(id);
    }
}
