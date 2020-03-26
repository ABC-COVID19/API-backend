package pt.tech4covid.service;

import pt.tech4covid.domain.ArticleType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ArticleType}.
 */
public interface ArticleTypeService {

    /**
     * Save a articleType.
     *
     * @param articleType the entity to save.
     * @return the persisted entity.
     */
    ArticleType save(ArticleType articleType);

    /**
     * Get all the articleTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ArticleType> findAll(Pageable pageable);

    /**
     * Get the "id" articleType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArticleType> findOne(Long id);

    /**
     * Delete the "id" articleType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
