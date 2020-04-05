package pt.tech4covid.repository;

import pt.tech4covid.domain.Revision;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Revision entity.
 */
@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long>, JpaSpecificationExecutor<Revision> {

    @Query(value = "select distinct revision from Revision revision left join fetch revision.ctrees",
        countQuery = "select count(distinct revision) from Revision revision")
    Page<Revision> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct revision from Revision revision left join fetch revision.ctrees")
    List<Revision> findAllWithEagerRelationships();

    @Query("select revision from Revision revision left join fetch revision.ctrees where revision.id =:id")
    Optional<Revision> findOneWithEagerRelationships(@Param("id") Long id);
}
