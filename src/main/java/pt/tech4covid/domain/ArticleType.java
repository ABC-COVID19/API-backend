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
 * A ArticleType.
 */
@Entity
@Table(name = "article_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArticleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "atype")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Revision> revisions = new HashSet<>();

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

    public ArticleType itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean isActive() {
        return active;
    }

    public ArticleType active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public ArticleType revisions(Set<Revision> revisions) {
        this.revisions = revisions;
        return this;
    }

    public ArticleType addRevision(Revision revision) {
        this.revisions.add(revision);
        revision.setAtype(this);
        return this;
    }

    public ArticleType removeRevision(Revision revision) {
        this.revisions.remove(revision);
        revision.setAtype(null);
        return this;
    }

    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleType)) {
            return false;
        }
        return id != null && id.equals(((ArticleType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArticleType{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
