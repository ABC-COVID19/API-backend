package pt.tech4covid.repository;

import pt.tech4covid.domain.PublicationSource;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PublicationSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicationSourceRepository extends JpaRepository<PublicationSource, Long>, JpaSpecificationExecutor<PublicationSource> {
}
