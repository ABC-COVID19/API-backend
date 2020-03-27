package pt.tech4covid.repository;

import pt.tech4covid.domain.ContentSource;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ContentSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentSourceRepository extends JpaRepository<ContentSource, Long> {
}
