package pt.tech4covid.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A SourceRepo.
 */
@Entity
@Table(name = "source_repo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SourceRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "srepo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public SourceRepo itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean isActive() {
        return active;
    }

    public SourceRepo active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public SourceRepo articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public SourceRepo addArticle(Article article) {
        this.articles.add(article);
        article.setSrepo(this);
        return this;
    }

    public SourceRepo removeArticle(Article article) {
        this.articles.remove(article);
        article.setSrepo(null);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceRepo)) {
            return false;
        }
        return id != null && id.equals(((SourceRepo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SourceRepo{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
