package pt.tech4covid.service.impl;

import pt.tech4covid.service.CategoryTreeService;
import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.repository.CategoryTreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CategoryTree}.
 */
@Service
@Transactional
public class CategoryTreeServiceImpl implements CategoryTreeService {

    private final Logger log = LoggerFactory.getLogger(CategoryTreeServiceImpl.class);

    private final CategoryTreeRepository categoryTreeRepository;

    public CategoryTreeServiceImpl(CategoryTreeRepository categoryTreeRepository) {
        this.categoryTreeRepository = categoryTreeRepository;
    }

    /**
     * Save a categoryTree.
     *
     * @param categoryTree the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CategoryTree save(CategoryTree categoryTree) {
        log.debug("Request to save CategoryTree : {}", categoryTree);
        return categoryTreeRepository.save(categoryTree);
    }

    /**
     * Get all the categoryTrees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CategoryTree> findAll(Pageable pageable) {
        log.debug("Request to get all CategoryTrees");
        return categoryTreeRepository.findAll(pageable);
    }

    /**
     * Get one categoryTree by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryTree> findOne(Long id) {
        log.debug("Request to get CategoryTree : {}", id);
        return categoryTreeRepository.findById(id);
    }

    /**
     * Delete the categoryTree by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoryTree : {}", id);
        categoryTreeRepository.deleteById(id);
    }
}
