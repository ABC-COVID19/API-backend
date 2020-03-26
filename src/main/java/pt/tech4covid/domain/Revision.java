package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    
    @Lob
    @Column(name = "summary", nullable = false)
    private String summary;

    @NotNull
    @Column(name = "reviewer", nullable = false)
    private String reviewer;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "review_state", nullable = false)
    private ReviewState reviewState;

    @Lob
    @Column(name = "return_notes")
    private String returnNotes;

    @NotNull
    @Column(name = "reviewed_by_peer", nullable = false)
    private Boolean reviewedByPeer;

    @Column(name = "community_votes")
    private Integer communityVotes;

    @ManyToOne
    @JsonIgnoreProperties("revisions")
    private ArticleType atype;

    @ManyToOne
    @JsonIgnoreProperties("revisions")
    private CategoryTree ctree;

    @ManyToOne
    @JsonIgnoreProperties("revisions")
    private Article article;

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

    public Boolean isActive() {
        return active;
    }

    public Revision active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getReturnNotes() {
        return returnNotes;
    }

    public Revision returnNotes(String returnNotes) {
        this.returnNotes = returnNotes;
        return this;
    }

    public void setReturnNotes(String returnNotes) {
        this.returnNotes = returnNotes;
    }

    public Boolean isReviewedByPeer() {
        return reviewedByPeer;
    }

    public Revision reviewedByPeer(Boolean reviewedByPeer) {
        this.reviewedByPeer = reviewedByPeer;
        return this;
    }

    public void setReviewedByPeer(Boolean reviewedByPeer) {
        this.reviewedByPeer = reviewedByPeer;
    }

    public Integer getCommunityVotes() {
        return communityVotes;
    }

    public Revision communityVotes(Integer communityVotes) {
        this.communityVotes = communityVotes;
        return this;
    }

    public void setCommunityVotes(Integer communityVotes) {
        this.communityVotes = communityVotes;
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

    public CategoryTree getCtree() {
        return ctree;
    }

    public Revision ctree(CategoryTree categoryTree) {
        this.ctree = categoryTree;
        return this;
    }

    public void setCtree(CategoryTree categoryTree) {
        this.ctree = categoryTree;
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
            ", reviewer='" + getReviewer() + "'" +
            ", active='" + isActive() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", reviewState='" + getReviewState() + "'" +
            ", returnNotes='" + getReturnNotes() + "'" +
            ", reviewedByPeer='" + isReviewedByPeer() + "'" +
            ", communityVotes=" + getCommunityVotes() +
            "}";
    }
}
