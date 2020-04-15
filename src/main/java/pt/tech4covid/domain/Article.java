package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

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

    @Column(name = "repo_article_id")
    private Integer repoArticleId;

    @Column(name = "repo_date")
    private LocalDate repoDate;

    @Lob
    @Column(name = "repo_keywords")
    private String repoKeywords;

    @Column(name = "article_date")
    private String articleDate;

    @Column(name = "article_title")
    private String articleTitle;

    @Lob
    @Column(name = "article_abstract")
    private String articleAbstract;

    @Column(name = "article_link")
    private String articleLink;

    @Column(name = "article_journal")
    private String articleJournal;

    @Column(name = "article_citation")
    private String articleCitation;

    @Column(name = "fetch_date")
    private LocalDate fetchDate;

    @OneToOne(mappedBy = "article")
    @JsonIgnore
    private Revision revision;

    @ManyToOne
    @JsonIgnoreProperties("articles")
    private SourceRepo srepo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRepoArticleId() {
        return repoArticleId;
    }

    public Article repoArticleId(Integer repoArticleId) {
        this.repoArticleId = repoArticleId;
        return this;
    }

    public void setRepoArticleId(Integer repoArticleId) {
        this.repoArticleId = repoArticleId;
    }

    public LocalDate getRepoDate() {
        return repoDate;
    }

    public Article repoDate(LocalDate repoDate) {
        this.repoDate = repoDate;
        return this;
    }

    public void setRepoDate(LocalDate repoDate) {
        this.repoDate = repoDate;
    }

    public String getRepoKeywords() {
        return repoKeywords;
    }

    public Article repoKeywords(String repoKeywords) {
        this.repoKeywords = repoKeywords;
        return this;
    }

    public void setRepoKeywords(String repoKeywords) {
        this.repoKeywords = repoKeywords;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public Article articleDate(String articleDate) {
        this.articleDate = articleDate;
        return this;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public Article articleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
        return this;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public Article articleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
        return this;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public Article articleLink(String articleLink) {
        this.articleLink = articleLink;
        return this;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleJournal() {
        return articleJournal;
    }

    public Article articleJournal(String articleJournal) {
        this.articleJournal = articleJournal;
        return this;
    }

    public void setArticleJournal(String articleJournal) {
        this.articleJournal = articleJournal;
    }

    public String getArticleCitation() {
        return articleCitation;
    }

    public Article articleCitation(String articleCitation) {
        this.articleCitation = articleCitation;
        return this;
    }

    public void setArticleCitation(String articleCitation) {
        this.articleCitation = articleCitation;
    }

    public LocalDate getFetchDate() {
        return fetchDate;
    }

    public Article fetchDate(LocalDate fetchDate) {
        this.fetchDate = fetchDate;
        return this;
    }

    public void setFetchDate(LocalDate fetchDate) {
        this.fetchDate = fetchDate;
    }

    public Revision getRevision() {
        return revision;
    }

    public Article revision(Revision revision) {
        this.revision = revision;
        return this;
    }

    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    public SourceRepo getSrepo() {
        return srepo;
    }

    public Article srepo(SourceRepo sourceRepo) {
        this.srepo = sourceRepo;
        return this;
    }

    public void setSrepo(SourceRepo sourceRepo) {
        this.srepo = sourceRepo;
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
            ", repoArticleId=" + getRepoArticleId() +
            ", repoDate='" + getRepoDate() + "'" +
            ", repoKeywords='" + getRepoKeywords() + "'" +
            ", articleDate='" + getArticleDate() + "'" +
            ", articleTitle='" + getArticleTitle() + "'" +
            ", articleAbstract='" + getArticleAbstract() + "'" +
            ", articleLink='" + getArticleLink() + "'" +
            ", articleJournal='" + getArticleJournal() + "'" +
            ", articleCitation='" + getArticleCitation() + "'" +
            ", fetchDate='" + getFetchDate() + "'" +
            "}";
    }
}
