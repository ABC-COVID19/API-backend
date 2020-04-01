package pt.tech4covid.repository;

import pt.tech4covid.domain.SourceRepo;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SourceRepo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceRepoRepository extends JpaRepository<SourceRepo, Long> {
}
