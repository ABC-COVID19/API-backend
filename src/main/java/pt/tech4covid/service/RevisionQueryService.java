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

import pt.tech4covid.domain.Revision;
import pt.tech4covid.domain.*; // for static metamodels
import pt.tech4covid.repository.RevisionRepository;
import pt.tech4covid.service.dto.RevisionCriteria;

/**
 * Service for executing complex queries for {@link Revision} entities in the database.
 * The main input is a {@link RevisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Revision} or a {@link Page} of {@link Revision} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RevisionQueryService extends QueryService<Revision> {

    private final Logger log = LoggerFactory.getLogger(RevisionQueryService.class);

    private final RevisionRepository revisionRepository;

    public RevisionQueryService(RevisionRepository revisionRepository) {
        this.revisionRepository = revisionRepository;
    }

    /**
     * Return a {@link List} of {@link Revision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Revision> findByCriteria(RevisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Revision> specification = createSpecification(criteria);
        return revisionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Revision} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Revision> findByCriteria(RevisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Revision> specification = createSpecification(criteria);
        return revisionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RevisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Revision> specification = createSpecification(criteria);
        return revisionRepository.count(specification);
    }

    /**
     * Function to convert {@link RevisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Revision> createSpecification(RevisionCriteria criteria) {
        Specification<Revision> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Revision_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Revision_.title));
            }
            if (criteria.getIsPeerReviewed() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPeerReviewed(), Revision_.isPeerReviewed));
            }
            if (criteria.getKeywords() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKeywords(), Revision_.keywords));
            }
            if (criteria.getSummary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSummary(), Revision_.summary));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Revision_.country));
            }
            if (criteria.getReviewDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewDate(), Revision_.reviewDate));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Revision_.author));
            }
            if (criteria.getReviewer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReviewer(), Revision_.reviewer));
            }
            if (criteria.getReviewState() != null) {
                specification = specification.and(buildSpecification(criteria.getReviewState(), Revision_.reviewState));
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(buildSpecification(criteria.getArticleId(),
                    root -> root.join(Revision_.article, JoinType.LEFT).get(Article_.id)));
            }
            if (criteria.getCtreeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCtreeId(),
                    root -> root.join(Revision_.ctrees, JoinType.LEFT).get(CategoryTree_.id)));
            }
            if (criteria.getAtypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getAtypeId(),
                    root -> root.join(Revision_.atype, JoinType.LEFT).get(ArticleType_.id)));
            }
        }
        return specification;
    }
}
