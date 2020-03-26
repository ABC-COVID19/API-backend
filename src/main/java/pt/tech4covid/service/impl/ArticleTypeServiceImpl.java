package pt.tech4covid.service.impl;

import pt.tech4covid.service.ArticleTypeService;
import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.repository.ArticleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ArticleType}.
 */
@Service
@Transactional
public class ArticleTypeServiceImpl implements ArticleTypeService {

    private final Logger log = LoggerFactory.getLogger(ArticleTypeServiceImpl.class);

    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeServiceImpl(ArticleTypeRepository articleTypeRepository) {
        this.articleTypeRepository = articleTypeRepository;
    }

    /**
     * Save a articleType.
     *
     * @param articleType the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ArticleType save(ArticleType articleType) {
        log.debug("Request to save ArticleType : {}", articleType);
        return articleTypeRepository.save(articleType);
    }

    /**
     * Get all the articleTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArticleType> findAll(Pageable pageable) {
        log.debug("Request to get all ArticleTypes");
        return articleTypeRepository.findAll(pageable);
    }

    /**
     * Get one articleType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleType> findOne(Long id) {
        log.debug("Request to get ArticleType : {}", id);
        return articleTypeRepository.findById(id);
    }

    /**
     * Delete the articleType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ArticleType : {}", id);
        articleTypeRepository.deleteById(id);
    }
}
