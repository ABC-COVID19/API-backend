package pt.tech4covid.repository;

import pt.tech4covid.domain.Newsletter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Newsletter entity.
 */
@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long>, JpaSpecificationExecutor<Newsletter> {

    @Query(value = "select distinct newsletter from Newsletter newsletter left join fetch newsletter.categoryTrees",
        countQuery = "select count(distinct newsletter) from Newsletter newsletter")
    Page<Newsletter> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct newsletter from Newsletter newsletter left join fetch newsletter.categoryTrees")
    List<Newsletter> findAllWithEagerRelationships();

    @Query("select newsletter from Newsletter newsletter left join fetch newsletter.categoryTrees where newsletter.id =:id")
    Optional<Newsletter> findOneWithEagerRelationships(@Param("id") Long id);
}
