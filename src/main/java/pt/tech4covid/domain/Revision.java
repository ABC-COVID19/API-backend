package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import pt.tech4covid.domain.enumeration.ReviewState;

/**
 * A Revision.
 */
@Entity
@Table(name = "revision")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Revision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;


    @Lob
    @Column(name = "summary", nullable = false)
    private String summary;

    @NotNull
    @Column(name = "is_peer_reviewed", nullable = false)
    private Boolean isPeerReviewed;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @Lob
    @Column(name = "review_notes")
    private String reviewNotes;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull
    @Column(name = "reviewer", nullable = false)
    private String reviewer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "review_state", nullable = false)
    private ReviewState reviewState;

    @OneToOne

    @MapsId
    @JoinColumn(name = "id")
    private Article article;

    @ManyToMany(fetch = FetchType.EAGER)
    //@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties("revisions")
    @JoinTable(name = "revision_ctree",
               joinColumns = @JoinColumn(name = "revision_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "ctree_id", referencedColumnName = "id"))
    private Set<CategoryTree> ctrees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("revisions")
    private ArticleType atype;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Revision title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public Revision summary(String summary) {
        this.summary = summary;
        return this;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean isIsPeerReviewed() {
        return isPeerReviewed;
    }

    public Revision isPeerReviewed(Boolean isPeerReviewed) {
        this.isPeerReviewed = isPeerReviewed;
        return this;
    }

    public void setIsPeerReviewed(Boolean isPeerReviewed) {
        this.isPeerReviewed = isPeerReviewed;
    }

    public String getCountry() {
        return country;
    }

    public Revision country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKeywords() {
        return keywords;
    }

    public Revision keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public Revision reviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
        return this;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public Revision reviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
        return this;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public String getAuthor() {
        return author;
    }

    public Revision author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewer() {
        return reviewer;
    }

    public Revision reviewer(String reviewer) {
        this.reviewer = reviewer;
        return this;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public ReviewState getReviewState() {
        return reviewState;
    }

    public Revision reviewState(ReviewState reviewState) {
        this.reviewState = reviewState;
        return this;
    }

    public void setReviewState(ReviewState reviewState) {
        this.reviewState = reviewState;
    }

    public Article getArticle() {
        return article;
    }

    public Revision article(Article article) {
        this.article = article;
        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Set<CategoryTree> getCtrees() {
        return ctrees;
    }

    public Revision ctrees(Set<CategoryTree> categoryTrees) {
        this.ctrees = categoryTrees;
        return this;
    }

    public Revision addCtree(CategoryTree categoryTree) {
        this.ctrees.add(categoryTree);
        categoryTree.getRevisions().add(this);
        return this;
    }

    public Revision removeCtree(CategoryTree categoryTree) {
        this.ctrees.remove(categoryTree);
        categoryTree.getRevisions().remove(this);
        return this;
    }

    public void setCtrees(Set<CategoryTree> categoryTrees) {
        this.ctrees = categoryTrees;
    }

    public ArticleType getAtype() {
        return atype;
    }

    public Revision atype(ArticleType articleType) {
        this.atype = articleType;
        return this;
    }

    public void setAtype(ArticleType articleType) {
        this.atype = articleType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Revision)) {
            return false;
        }
        return id != null && id.equals(((Revision) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Revision{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", summary='" + getSummary() + "'" +
            ", isPeerReviewed='" + isIsPeerReviewed() + "'" +
            ", country='" + getCountry() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", reviewDate='" + getReviewDate() + "'" +
            ", reviewNotes='" + getReviewNotes() + "'" +
            ", author='" + getAuthor() + "'" +
            ", reviewer='" + getReviewer() + "'" +
            ", reviewState='" + getReviewState() + "'" +
            "}";
    }
}
