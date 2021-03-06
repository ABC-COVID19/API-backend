package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

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

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "active")
    private Boolean active;

    // Changed default file
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties("parent")
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CategoryTree> children = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("children")
    private CategoryTree parent;

    @ManyToMany(mappedBy = "categoryTrees")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Newsletter> newsletters = new HashSet<>();

    @ManyToMany(mappedBy = "ctrees")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
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

    public CategoryTree itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Set<CategoryTree> getChildren() {
        return children;
    }

    public CategoryTree children(Set<CategoryTree> categoryTrees) {
        this.children = categoryTrees;
        return this;
    }

    public CategoryTree addChildren(CategoryTree categoryTree) {
        this.children.add(categoryTree);
        categoryTree.setParent(this);
        return this;
    }

    public CategoryTree removeChildren(CategoryTree categoryTree) {
        this.children.remove(categoryTree);
        categoryTree.setParent(null);
        return this;
    }

    public void setChildren(Set<CategoryTree> categoryTrees) {
        this.children = categoryTrees;
    }

    public CategoryTree getParent() {
        return parent;
    }

    public CategoryTree parent(CategoryTree categoryTree) {
        this.parent = categoryTree;
        return this;
    }

    public void setParent(CategoryTree categoryTree) {
        this.parent = categoryTree;
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

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public CategoryTree revisions(Set<Revision> revisions) {
        this.revisions = revisions;
        return this;
    }

    public CategoryTree addRevision(Revision revision) {
        this.revisions.add(revision);
        revision.getCtrees().add(this);
        return this;
    }

    public CategoryTree removeRevision(Revision revision) {
        this.revisions.remove(revision);
        revision.getCtrees().remove(this);
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
            ", itemName='" + getItemName() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
}
