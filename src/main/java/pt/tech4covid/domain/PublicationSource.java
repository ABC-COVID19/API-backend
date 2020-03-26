package pt.tech4covid.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A PublicationSource.
 */
@Entity
@Table(name = "publication_source")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PublicationSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_name")
    private String sourceName;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "pubName")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Article> names = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public PublicationSource sourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Boolean isActive() {
        return active;
    }

    public PublicationSource active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Article> getNames() {
        return names;
    }

    public PublicationSource names(Set<Article> articles) {
        this.names = articles;
        return this;
    }

    public PublicationSource addName(Article article) {
        this.names.add(article);
        article.setPubName(this);
        return this;
    }

    public PublicationSource removeName(Article article) {
        this.names.remove(article);
        article.setPubName(null);
        return this;
    }

    public void setNames(Set<Article> articles) {
        this.names = articles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublicationSource)) {
            return false;
        }
        return id != null && id.equals(((PublicationSource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PublicationSource{" +
            "id=" + getId() +
            ", sourceName='" + getSourceName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
