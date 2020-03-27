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

import pt.tech4covid.domain.ArticleType;
import pt.tech4covid.domain.*; // for static metamodels
import pt.tech4covid.repository.ArticleTypeRepository;
import pt.tech4covid.service.dto.ArticleTypeCriteria;

/**
 * Service for executing complex queries for {@link ArticleType} entities in the database.
 * The main input is a {@link ArticleTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArticleType} or a {@link Page} of {@link ArticleType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleTypeQueryService extends QueryService<ArticleType> {

    private final Logger log = LoggerFactory.getLogger(ArticleTypeQueryService.class);

    private final ArticleTypeRepository articleTypeRepository;

    public ArticleTypeQueryService(ArticleTypeRepository articleTypeRepository) {
        this.articleTypeRepository = articleTypeRepository;
    }

    /**
     * Return a {@link List} of {@link ArticleType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArticleType> findByCriteria(ArticleTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ArticleType> specification = createSpecification(criteria);
        return articleTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ArticleType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleType> findByCriteria(ArticleTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArticleType> specification = createSpecification(criteria);
        return articleTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ArticleType> specification = createSpecification(criteria);
        return articleTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArticleType> createSpecification(ArticleTypeCriteria criteria) {
        Specification<ArticleType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ArticleType_.id));
            }
            if (criteria.getItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemName(), ArticleType_.itemName));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), ArticleType_.active));
            }
            if (criteria.getRevisionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRevisionId(),
                    root -> root.join(ArticleType_.revisions, JoinType.LEFT).get(Revision_.id)));
            }
        }
        return specification;
    }
}
