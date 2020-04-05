package pt.tech4covid.service;

import pt.tech4covid.domain.ArticleType;

import java.util.List;
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
     * @return the list of entities.
     */
    List<ArticleType> findAll();

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
