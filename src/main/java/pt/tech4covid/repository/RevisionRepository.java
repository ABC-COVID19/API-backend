package pt.tech4covid.repository;

import pt.tech4covid.domain.Revision;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Revision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {
}
