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

import pt.tech4covid.domain.Article;
import pt.tech4covid.domain.*; // for static metamodels
import pt.tech4covid.repository.ArticleRepository;
import pt.tech4covid.service.dto.ArticleCriteria;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Article} or a {@link Page} of {@link Article} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private final Logger log = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    public ArticleQueryService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Return a {@link List} of {@link Article} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Article> findByCriteria(ArticleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Article} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Article> findByCriteria(ArticleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getFetchedFrom() != null) {
                specification = specification.and(buildSpecification(criteria.getFetchedFrom(), Article_.fetchedFrom));
            }
            if (criteria.getSourceID() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSourceID(), Article_.sourceID));
            }
            if (criteria.getSourceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSourceDate(), Article_.sourceDate));
            }
            if (criteria.getSourceTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceTitle(), Article_.sourceTitle));
            }
            if (criteria.getSourceAbstract() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceAbstract(), Article_.sourceAbstract));
            }
            if (criteria.getImportDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getImportDate(), Article_.importDate));
            }
            if (criteria.getOutboundLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutboundLink(), Article_.outboundLink));
            }
            if (criteria.getReviewState() != null) {
                specification = specification.and(buildSpecification(criteria.getReviewState(), Article_.reviewState));
            }
            if (criteria.getRevisionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRevisionId(),
                    root -> root.join(Article_.revisions, JoinType.LEFT).get(Revision_.id)));
            }
            if (criteria.getPubNameId() != null) {
                specification = specification.and(buildSpecification(criteria.getPubNameId(),
                    root -> root.join(Article_.pubName, JoinType.LEFT).get(PublicationSource_.id)));
            }
        }
        return specification;
    }
}
