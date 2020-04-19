package pt.tech4covid.service;

import pt.tech4covid.domain.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Article}.
 */
public interface ArticleService {

    /**
     * Save a article.
     *
     * @param article the entity to save.
     * @return the persisted entity.
     */
    Article save(Article article);

    /**
     * Get all the articles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Article> findAll(Pageable pageable);
    /**
     * Get all the ArticleDTO where Revision is {@code null}.
     *
     * @return the list of entities.
     */
    List<Article> findAllWhereRevisionIsNull();

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Article> findOne(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
