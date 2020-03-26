package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "reviewer")
    private String reviewer;

    @Column(name = "active")
    private Boolean active;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Lob
    @Column(name = "abs_revision")
    private String absRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_state")
    private ReviewState reviewState;

    @Column(name = "return_notes")
    private String returnNotes;

    @Column(name = "reviewed_by_peer")
    private Boolean reviewedByPeer;

    @Column(name = "community_votes")
    private Integer communityVotes;

    @ManyToOne
    @JsonIgnoreProperties("revisions")
    private Article article;

    @ManyToOne
    @JsonIgnoreProperties("names")
    private ArticleType type;

    @ManyToOne
    @JsonIgnoreProperties("names")
    private CategoryTree area;

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

    public String getAbsRevision() {
        return absRevision;
    }

    public Revision absRevision(String absRevision) {
        this.absRevision = absRevision;
        return this;
    }

    public void setAbsRevision(String absRevision) {
        this.absRevision = absRevision;
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

    public ArticleType getType() {
        return type;
    }

    public Revision type(ArticleType articleType) {
        this.type = articleType;
        return this;
    }

    public void setType(ArticleType articleType) {
        this.type = articleType;
    }

    public CategoryTree getArea() {
        return area;
    }

    public Revision area(CategoryTree categoryTree) {
        this.area = categoryTree;
        return this;
    }

    public void setArea(CategoryTree categoryTree) {
        this.area = categoryTree;
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
            ", absRevision='" + getAbsRevision() + "'" +
            ", reviewState='" + getReviewState() + "'" +
            ", returnNotes='" + getReturnNotes() + "'" +
            ", reviewedByPeer='" + isReviewedByPeer() + "'" +
            ", communityVotes=" + getCommunityVotes() +
            "}";
    }
}
