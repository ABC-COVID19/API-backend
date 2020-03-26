package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A CategoryTree.
 */
@Entity
@Table(name = "category_tree")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CategoryTree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "area")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Revision> names = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("categoryTrees")
    private CategoryTree child;

    @ManyToMany(mappedBy = "categoryTrees")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Newsletter> newsletters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CategoryTree name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public CategoryTree active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Revision> getNames() {
        return names;
    }

    public CategoryTree names(Set<Revision> revisions) {
        this.names = revisions;
        return this;
    }

    public CategoryTree addName(Revision revision) {
        this.names.add(revision);
        revision.setArea(this);
        return this;
    }

    public CategoryTree removeName(Revision revision) {
        this.names.remove(revision);
        revision.setArea(null);
        return this;
    }

    public void setNames(Set<Revision> revisions) {
        this.names = revisions;
    }

    public CategoryTree getChild() {
        return child;
    }

    public CategoryTree child(CategoryTree categoryTree) {
        this.child = categoryTree;
        return this;
    }

    public void setChild(CategoryTree categoryTree) {
        this.child = categoryTree;
    }

    public Set<Newsletter> getNewsletters() {
        return newsletters;
    }

    public CategoryTree newsletters(Set<Newsletter> newsletters) {
        this.newsletters = newsletters;
        return this;
    }

    public CategoryTree addNewsletter(Newsletter newsletter) {
        this.newsletters.add(newsletter);
        newsletter.getCategoryTrees().add(this);
        return this;
    }

    public CategoryTree removeNewsletter(Newsletter newsletter) {
        this.newsletters.remove(newsletter);
        newsletter.getCategoryTrees().remove(this);
        return this;
    }

    public void setNewsletters(Set<Newsletter> newsletters) {
        this.newsletters = newsletters;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryTree)) {
            return false;
        }
        return id != null && id.equals(((CategoryTree) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CategoryTree{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
