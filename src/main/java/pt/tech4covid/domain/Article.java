package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @NotNull
    @Column(name = "source_id", nullable = false)
    private Integer sourceID;

    @NotNull
    @Column(name = "source_date", nullable = false)
    private LocalDate sourceDate;

    @Column(name = "source_title")
    private String sourceTitle;

    @Column(name = "source_abstract")
    private String sourceAbstract;

    @Column(name = "pubmed_date")
    private LocalDate pubmedDate;

    @Column(name = "official_pub_date")
    private LocalDate officialPubDate;

    @Column(name = "doi")
    private String doi;

    @Column(name = "journal")
    private String journal;

    @Column(name = "citation")
    private String citation;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "review_state", nullable = false)
    private ReviewState reviewState;

    @OneToMany(mappedBy = "article")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Revision> revisions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private ContentSource cntsource;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getPubmedDate() {
        return pubmedDate;
    }

    public Article pubmedDate(LocalDate pubmedDate) {
        this.pubmedDate = pubmedDate;
        return this;
    }

    public void setPubmedDate(LocalDate pubmedDate) {
        this.pubmedDate = pubmedDate;
    }

    public LocalDate getOfficialPubDate() {
        return officialPubDate;
    }

    public Article officialPubDate(LocalDate officialPubDate) {
        this.officialPubDate = officialPubDate;
        return this;
    }

    public void setOfficialPubDate(LocalDate officialPubDate) {
        this.officialPubDate = officialPubDate;
    }

    public String getDoi() {
        return doi;
    }

    public Article doi(String doi) {
        this.doi = doi;
        return this;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getJournal() {
        return journal;
    }

    public Article journal(String journal) {
        this.journal = journal;
        return this;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getCitation() {
        return citation;
    }

    public Article citation(String citation) {
        this.citation = citation;
        return this;
    }

    public void setCitation(String citation) {
        this.citation = citation;
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

    public ContentSource getCntsource() {
        return cntsource;
    }

    public Article cntsource(ContentSource contentSource) {
        this.cntsource = contentSource;
        return this;
    }

    public void setCntsource(ContentSource contentSource) {
        this.cntsource = contentSource;
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
            ", sourceID=" + getSourceID() +
            ", sourceDate='" + getSourceDate() + "'" +
            ", sourceTitle='" + getSourceTitle() + "'" +
            ", sourceAbstract='" + getSourceAbstract() + "'" +
            ", pubmedDate='" + getPubmedDate() + "'" +
            ", officialPubDate='" + getOfficialPubDate() + "'" +
            ", doi='" + getDoi() + "'" +
            ", journal='" + getJournal() + "'" +
            ", citation='" + getCitation() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", reviewState='" + getReviewState() + "'" +
            "}";
    }
}
