package pt.tech4covid.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Newsletter.
 */
@Entity
@Table(name = "newsletter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Newsletter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "rgpd_auth")
    private Boolean rgpdAuth;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "newsletter_category_tree",
               joinColumns = @JoinColumn(name = "newsletter_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "category_tree_id", referencedColumnName = "id"))
    private Set<CategoryTree> categoryTrees = new HashSet<>();

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

    public Newsletter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Newsletter email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public Newsletter registrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Boolean isRgpdAuth() {
        return rgpdAuth;
    }

    public Newsletter rgpdAuth(Boolean rgpdAuth) {
        this.rgpdAuth = rgpdAuth;
        return this;
    }

    public void setRgpdAuth(Boolean rgpdAuth) {
        this.rgpdAuth = rgpdAuth;
    }

    public Set<CategoryTree> getCategoryTrees() {
        return categoryTrees;
    }

    public Newsletter categoryTrees(Set<CategoryTree> categoryTrees) {
        this.categoryTrees = categoryTrees;
        return this;
    }

    public Newsletter addCategoryTree(CategoryTree categoryTree) {
        this.categoryTrees.add(categoryTree);
        categoryTree.getNewsletters().add(this);
        return this;
    }

    public Newsletter removeCategoryTree(CategoryTree categoryTree) {
        this.categoryTrees.remove(categoryTree);
        categoryTree.getNewsletters().remove(this);
        return this;
    }

    public void setCategoryTrees(Set<CategoryTree> categoryTrees) {
        this.categoryTrees = categoryTrees;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Newsletter)) {
            return false;
        }
        return id != null && id.equals(((Newsletter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Newsletter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", rgpdAuth='" + isRgpdAuth() + "'" +
            "}";
    }
}
