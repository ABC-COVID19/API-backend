package pt.tech4covid.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pt.tech4covid.domain.CategoryTree;
import pt.tech4covid.domain.*; // for static metamodels
import pt.tech4covid.repository.CategoryTreeRepository;
import pt.tech4covid.service.dto.CategoryTreeCriteria;

/**
 * Service for executing complex queries for {@link CategoryTree} entities in the database.
 * The main input is a {@link CategoryTreeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CategoryTree} or a {@link Page} of {@link CategoryTree} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoryTreeQueryService extends QueryService<CategoryTree> {

    private final Logger log = LoggerFactory.getLogger(CategoryTreeQueryService.class);

    private final CategoryTreeRepository categoryTreeRepository;

    public CategoryTreeQueryService(CategoryTreeRepository categoryTreeRepository) {
        this.categoryTreeRepository = categoryTreeRepository;
    }

    /**
     * Return a {@link List} of {@link CategoryTree} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CategoryTree> findByCriteria(CategoryTreeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CategoryTree> specification = createSpecification(criteria);
        return categoryTreeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CategoryTree} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoryTree> findByCriteria(CategoryTreeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CategoryTree> specification = createSpecification(criteria);
        return categoryTreeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoryTreeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CategoryTree> specification = createSpecification(criteria);
        return categoryTreeRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoryTreeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CategoryTree> createSpecification(CategoryTreeCriteria criteria) {
        Specification<CategoryTree> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CategoryTree_.id));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), CategoryTree_.itemName));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), CategoryTree_.active));
            }
            if (criteria.getChildId() != null) {
                specification = specification.and(buildSpecification(criteria.getChildId(),
                    root -> root.join(CategoryTree_.child, JoinType.LEFT).get(CategoryTree_.id)));
            }
            if (criteria.getNewsletterId() != null) {
                specification = specification.and(buildSpecification(criteria.getNewsletterId(),
                    root -> root.join(CategoryTree_.newsletters, JoinType.LEFT).get(Newsletter_.id)));
            }
            if (criteria.getRevisionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRevisionId(),
                    root -> root.join(CategoryTree_.revisions, JoinType.LEFT).get(Revision_.id)));
            }
        }
        return specification;
    }
}
