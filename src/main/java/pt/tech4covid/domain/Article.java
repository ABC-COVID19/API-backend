package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import pt.tech4covid.domain.enumeration.ContentSource;

import pt.tech4covid.domain.enumeration.ReviewState;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "fetched_from")
    private ContentSource fetchedFrom;

    @Column(name = "source_id")
    private Integer sourceID;

    @Column(name = "source_date")
    private LocalDate sourceDate;

    @Column(name = "source_title")
    private String sourceTitle;

    @Column(name = "source_abstract")
    private String sourceAbstract;

    @Column(name = "import_date")
    private LocalDate importDate;

    @Column(name = "outbound_link")
    private String outboundLink;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_state")
    private ReviewState reviewState;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Revision> revisions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("names")
    private PublicationSource pubName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentSource getFetchedFrom() {
        return fetchedFrom;
    }

    public Article fetchedFrom(ContentSource fetchedFrom) {
        this.fetchedFrom = fetchedFrom;
        return this;
    }

    public void setFetchedFrom(ContentSource fetchedFrom) {
        this.fetchedFrom = fetchedFrom;
    }

    public Integer getSourceID() {
        return sourceID;
    }

    public Article sourceID(Integer sourceID) {
        this.sourceID = sourceID;
        return this;
    }

    public void setSourceID(Integer sourceID) {
        this.sourceID = sourceID;
    }

    public LocalDate getSourceDate() {
        return sourceDate;
    }

    public Article sourceDate(LocalDate sourceDate) {
        this.sourceDate = sourceDate;
        return this;
    }

    public void setSourceDate(LocalDate sourceDate) {
        this.sourceDate = sourceDate;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public Article sourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
        return this;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getSourceAbstract() {
        return sourceAbstract;
    }

    public Article sourceAbstract(String sourceAbstract) {
        this.sourceAbstract = sourceAbstract;
        return this;
    }

    public void setSourceAbstract(String sourceAbstract) {
        this.sourceAbstract = sourceAbstract;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public Article importDate(LocalDate importDate) {
        this.importDate = importDate;
        return this;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public String getOutboundLink() {
        return outboundLink;
    }

    public Article outboundLink(String outboundLink) {
        this.outboundLink = outboundLink;
        return this;
    }

    public void setOutboundLink(String outboundLink) {
        this.outboundLink = outboundLink;
    }

    public String getKeywords() {
        return keywords;
    }

    public Article keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public ReviewState getReviewState() {
        return reviewState;
    }

    public Article reviewState(ReviewState reviewState) {
        this.reviewState = reviewState;
        return this;
    }

    public void setReviewState(ReviewState reviewState) {
        this.reviewState = reviewState;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public Article revisions(Set<Revision> revisions) {
        this.revisions = revisions;
        return this;
    }

    public Article addRevision(Revision revision) {
        this.revisions.add(revision);
        revision.setArticle(this);
        return this;
    }

    public Article removeRevision(Revision revision) {
        this.revisions.remove(revision);
        revision.setArticle(null);
        return this;
    }

    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }

    public PublicationSource getPubName() {
        return pubName;
    }

    public Article pubName(PublicationSource publicationSource) {
        this.pubName = publicationSource;
        return this;
    }

    public void setPubName(PublicationSource publicationSource) {
        this.pubName = publicationSource;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", fetchedFrom='" + getFetchedFrom() + "'" +
            ", sourceID=" + getSourceID() +
            ", sourceDate='" + getSourceDate() + "'" +
            ", sourceTitle='" + getSourceTitle() + "'" +
            ", sourceAbstract='" + getSourceAbstract() + "'" +
            ", importDate='" + getImportDate() + "'" +
            ", outboundLink='" + getOutboundLink() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", reviewState='" + getReviewState() + "'" +
            "}";
    }
}
