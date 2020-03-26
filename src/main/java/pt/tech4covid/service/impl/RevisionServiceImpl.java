package pt.tech4covid.service.impl;

import pt.tech4covid.service.RevisionService;
import pt.tech4covid.domain.Revision;
import pt.tech4covid.repository.RevisionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Revision}.
 */
@Service
@Transactional
public class RevisionServiceImpl implements RevisionService {

    private final Logger log = LoggerFactory.getLogger(RevisionServiceImpl.class);

    private final RevisionRepository revisionRepository;

    public RevisionServiceImpl(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    /**
     * Save a revision.
     *
     * @param revision the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Revision save(Revision revision) {
        log.debug("Request to save Revision : {}", revision);
        return revisionRepository.save(revision);
    }

    /**
     * Get all the revisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Revision> findAll(Pageable pageable) {
        log.debug("Request to get all Revisions");
        return revisionRepository.findAll(pageable);
    }

    /**
     * Get one revision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Revision> findOne(Long id) {
        log.debug("Request to get Revision : {}", id);
        return revisionRepository.findById(id);
    }

    /**
     * Delete the revision by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Revision : {}", id);
        revisionRepository.deleteById(id);
    }
}
