package pt.tech4covid.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.tech4covid.domain.Article} entity. This class is used
 * in {@link pt.tech4covid.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ArticleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter repoArticleId;

    private LocalDateFilter repoDate;

    private StringFilter articleDate;

    private StringFilter articleTitle;

    private StringFilter articleLink;

    private StringFilter articleJournal;

    private StringFilter articleCitation;

    private LocalDateFilter fetchDate;

    private LongFilter revisionId;

    private LongFilter srepoId;

    private BooleanFilter hasRevision;

    public ArticleCriteria() {
    }

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.repoArticleId = other.repoArticleId == null ? null : other.repoArticleId.copy();
        this.repoDate = other.repoDate == null ? null : other.repoDate.copy();
        this.articleDate = other.articleDate == null ? null : other.articleDate.copy();
        this.articleTitle = other.articleTitle == null ? null : other.articleTitle.copy();
        this.articleLink = other.articleLink == null ? null : other.articleLink.copy();
        this.articleJournal = other.articleJournal == null ? null : other.articleJournal.copy();
        this.articleCitation = other.articleCitation == null ? null : other.articleCitation.copy();
        this.fetchDate = other.fetchDate == null ? null : other.fetchDate.copy();
        this.revisionId = other.revisionId == null ? null : other.revisionId.copy();
        this.srepoId = other.srepoId == null ? null : other.srepoId.copy();
        this.hasRevision = other.hasRevision == null ? null : other.hasRevision.copy();
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getRepoArticleId() {
        return repoArticleId;
    }

    public void setRepoArticleId(IntegerFilter repoArticleId) {
        this.repoArticleId = repoArticleId;
    }

    public LocalDateFilter getRepoDate() {
        return repoDate;
    }

    public void setRepoDate(LocalDateFilter repoDate) {
        this.repoDate = repoDate;
    }

    public StringFilter getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(StringFilter articleDate) {
        this.articleDate = articleDate;
    }

    public StringFilter getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(StringFilter articleTitle) {
        this.articleTitle = articleTitle;
    }

    public StringFilter getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(StringFilter articleLink) {
        this.articleLink = articleLink;
    }

    public StringFilter getArticleJournal() {
        return articleJournal;
    }

    public void setArticleJournal(StringFilter articleJournal) {
        this.articleJournal = articleJournal;
    }

    public StringFilter getArticleCitation() {
        return articleCitation;
    }

    public void setArticleCitation(StringFilter articleCitation) {
        this.articleCitation = articleCitation;
    }

    public LocalDateFilter getFetchDate() {
        return fetchDate;
    }

    public void setFetchDate(LocalDateFilter fetchDate) {
        this.fetchDate = fetchDate;
    }

    public LongFilter getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(LongFilter revisionId) {
        this.revisionId = revisionId;
    }

    public LongFilter getSrepoId() {
        return srepoId;
    }

    public void setSrepoId(LongFilter srepoId) {
        this.srepoId = srepoId;
    }

    public BooleanFilter getHasRevision() {
        return hasRevision;
    }

    public void setHasRevision(BooleanFilter hasRevision) {
        this.hasRevision = hasRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArticleCriteria that = (ArticleCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(repoArticleId, that.repoArticleId) &&
                Objects.equals(repoDate, that.repoDate) &&
                Objects.equals(articleDate, that.articleDate) &&
                Objects.equals(articleTitle, that.articleTitle) &&
                Objects.equals(articleLink, that.articleLink) &&
                Objects.equals(articleJournal, that.articleJournal) &&
                Objects.equals(articleCitation, that.articleCitation) &&
                Objects.equals(fetchDate, that.fetchDate) &&
                Objects.equals(revisionId, that.revisionId) &&
                Objects.equals(srepoId, that.srepoId) &&
                Objects.equals(hasRevision, that.hasRevision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            repoArticleId,
            repoDate,
            articleDate,
            articleTitle,
            articleLink,
            articleJournal,
            articleCitation,
            fetchDate,
            revisionId,
            srepoId,
            hasRevision
        );
    }

    @Override
    public String toString() {
        return "ArticleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (repoArticleId != null ? "repoArticleId=" + repoArticleId + ", " : "") +
            (repoDate != null ? "repoDate=" + repoDate + ", " : "") +
            (articleDate != null ? "articleDate=" + articleDate + ", " : "") +
            (articleTitle != null ? "articleTitle=" + articleTitle + ", " : "") +
            (articleLink != null ? "articleLink=" + articleLink + ", " : "") +
            (articleJournal != null ? "articleJournal=" + articleJournal + ", " : "") +
            (articleCitation != null ? "articleCitation=" + articleCitation + ", " : "") +
            (fetchDate != null ? "fetchDate=" + fetchDate + ", " : "") +
            (revisionId != null ? "revisionId=" + revisionId + ", " : "") +
            (srepoId != null ? "srepoId=" + srepoId + ", " : "") +
            (hasRevision != null ? "hasRevision=" + hasRevision + ", " : "") +
            "}";
    }

}
