package pt.tech4covid.repository;

import pt.tech4covid.domain.ArticleType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ArticleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleTypeRepository extends JpaRepository<ArticleType, Long> {
}
