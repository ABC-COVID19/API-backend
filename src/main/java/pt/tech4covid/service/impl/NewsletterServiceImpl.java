package pt.tech4covid.service.impl;

import pt.tech4covid.service.NewsletterService;
import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.repository.NewsletterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Newsletter}.
 */
@Service
@Transactional
public class NewsletterServiceImpl implements NewsletterService {

    private final Logger log = LoggerFactory.getLogger(NewsletterServiceImpl.class);

    private final NewsletterRepository newsletterRepository;

    public NewsletterServiceImpl(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    /**
     * Save a newsletter.
     *
     * @param newsletter the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Newsletter save(Newsletter newsletter) {
        log.debug("Request to save Newsletter : {}", newsletter);
        return newsletterRepository.save(newsletter);
    }

    /**
     * Get all the newsletters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Newsletter> findAll(Pageable pageable) {
        log.debug("Request to get all Newsletters");
        return newsletterRepository.findAll(pageable);
    }

    /**
     * Get all the newsletters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Newsletter> findAllWithEagerRelationships(Pageable pageable) {
        return newsletterRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one newsletter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Newsletter> findOne(Long id) {
        log.debug("Request to get Newsletter : {}", id);
        return newsletterRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the newsletter by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Newsletter : {}", id);
        newsletterRepository.deleteById(id);
    }

    @Override
    public long count() {
        return newsletterRepository.count();
    }
}
