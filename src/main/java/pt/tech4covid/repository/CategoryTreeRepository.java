package pt.tech4covid.repository;

import pt.tech4covid.domain.CategoryTree;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CategoryTree entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryTreeRepository extends JpaRepository<CategoryTree, Long>, JpaSpecificationExecutor<CategoryTree> {
}
