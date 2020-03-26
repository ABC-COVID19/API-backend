package pt.tech4covid.service.impl;

import pt.tech4covid.service.ContentSourceService;
import pt.tech4covid.domain.ContentSource;
import pt.tech4covid.repository.ContentSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ContentSource}.
 */
@Service
@Transactional
public class ContentSourceServiceImpl implements ContentSourceService {

    private final Logger log = LoggerFactory.getLogger(ContentSourceServiceImpl.class);

    private final ContentSourceRepository contentSourceRepository;

    public ContentSourceServiceImpl(ContentSourceRepository contentSourceRepository) {
        this.contentSourceRepository = contentSourceRepository;
    }

    /**
     * Save a contentSource.
     *
     * @param contentSource the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ContentSource save(ContentSource contentSource) {
        log.debug("Request to save ContentSource : {}", contentSource);
        return contentSourceRepository.save(contentSource);
    }

    /**
     * Get all the contentSources.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContentSource> findAll() {
        log.debug("Request to get all ContentSources");
        return contentSourceRepository.findAll();
    }

    /**
     * Get one contentSource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ContentSource> findOne(Long id) {
        log.debug("Request to get ContentSource : {}", id);
        return contentSourceRepository.findById(id);
    }

    /**
     * Delete the contentSource by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContentSource : {}", id);
        contentSourceRepository.deleteById(id);
    }
}
