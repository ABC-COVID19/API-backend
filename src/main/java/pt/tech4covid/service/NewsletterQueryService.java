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

import pt.tech4covid.domain.Newsletter;
import pt.tech4covid.domain.*; // for static metamodels
import pt.tech4covid.repository.NewsletterRepository;
import pt.tech4covid.service.dto.NewsletterCriteria;

/**
 * Service for executing complex queries for {@link Newsletter} entities in the database.
 * The main input is a {@link NewsletterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Newsletter} or a {@link Page} of {@link Newsletter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NewsletterQueryService extends QueryService<Newsletter> {

    private final Logger log = LoggerFactory.getLogger(NewsletterQueryService.class);

    private final NewsletterRepository newsletterRepository;

    public NewsletterQueryService(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    /**
     * Return a {@link List} of {@link Newsletter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Newsletter> findByCriteria(NewsletterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Newsletter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Newsletter> findByCriteria(NewsletterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NewsletterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Newsletter> specification = createSpecification(criteria);
        return newsletterRepository.count(specification);
    }

    /**
     * Function to convert {@link NewsletterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Newsletter> createSpecification(NewsletterCriteria criteria) {
        Specification<Newsletter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Newsletter_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Newsletter_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Newsletter_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Newsletter_.email));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), Newsletter_.registrationDate));
            }
            if (criteria.getRgpdAuth() != null) {
                specification = specification.and(buildSpecification(criteria.getRgpdAuth(), Newsletter_.rgpdAuth));
            }
            if (criteria.getCategoryTreeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryTreeId(),
                    root -> root.join(Newsletter_.categoryTrees, JoinType.LEFT).get(CategoryTree_.id)));
            }
        }
        return specification;
    }
}
