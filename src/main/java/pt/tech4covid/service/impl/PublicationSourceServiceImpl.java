package pt.tech4covid.service.impl;

import pt.tech4covid.service.PublicationSourceService;
import pt.tech4covid.domain.PublicationSource;
import pt.tech4covid.repository.PublicationSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PublicationSource}.
 */
@Service
@Transactional
public class PublicationSourceServiceImpl implements PublicationSourceService {

    private final Logger log = LoggerFactory.getLogger(PublicationSourceServiceImpl.class);

    private final PublicationSourceRepository publicationSourceRepository;

    public PublicationSourceServiceImpl(PublicationSourceRepository publicationSourceRepository) {
        this.publicationSourceRepository = publicationSourceRepository;
    }

    /**
     * Save a publicationSource.
     *
     * @param publicationSource the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PublicationSource save(PublicationSource publicationSource) {
        log.debug("Request to save PublicationSource : {}", publicationSource);
        return publicationSourceRepository.save(publicationSource);
    }

    /**
     * Get all the publicationSources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PublicationSource> findAll(Pageable pageable) {
        log.debug("Request to get all PublicationSources");
        return publicationSourceRepository.findAll(pageable);
    }

    /**
     * Get one publicationSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PublicationSource> findOne(Long id) {
        log.debug("Request to get PublicationSource : {}", id);
        return publicationSourceRepository.findById(id);
    }

    /**
     * Delete the publicationSource by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PublicationSource : {}", id);
        publicationSourceRepository.deleteById(id);
    }
}
