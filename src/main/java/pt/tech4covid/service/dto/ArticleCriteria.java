package pt.tech4covid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pt.tech4covid.domain.enumeration.ReviewState;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

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
    /**
     * Class for filtering ReviewState
     */
    public static class ReviewStateFilter extends Filter<ReviewState> {

        public ReviewStateFilter() {
        }

        public ReviewStateFilter(ReviewStateFilter filter) {
            super(filter);
        }

        @Override
        public ReviewStateFilter copy() {
            return new ReviewStateFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter repoArticleId;

    private LocalDateFilter repoDate;

    private LocalDateFilter articleDate;

    private StringFilter articleTitle;

    private StringFilter articleDoi;

    private StringFilter articleJournal;

    private LocalDateFilter fetchDate;

    private StringFilter citation;

    private ReviewStateFilter reviewState;

    private LongFilter revisionId;

    private LongFilter srepoId;

    public ArticleCriteria() {
    }

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.repoArticleId = other.repoArticleId == null ? null : other.repoArticleId.copy();
        this.repoDate = other.repoDate == null ? null : other.repoDate.copy();
        this.articleDate = other.articleDate == null ? null : other.articleDate.copy();
        this.articleTitle = other.articleTitle == null ? null : other.articleTitle.copy();
        this.articleDoi = other.articleDoi == null ? null : other.articleDoi.copy();
        this.articleJournal = other.articleJournal == null ? null : other.articleJournal.copy();
        this.fetchDate = other.fetchDate == null ? null : other.fetchDate.copy();
        this.citation = other.citation == null ? null : other.citation.copy();
        this.reviewState = other.reviewState == null ? null : other.reviewState.copy();
        this.revisionId = other.revisionId == null ? null : other.revisionId.copy();
        this.srepoId = other.srepoId == null ? null : other.srepoId.copy();
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

    public LocalDateFilter getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(LocalDateFilter articleDate) {
        this.articleDate = articleDate;
    }

    public StringFilter getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(StringFilter articleTitle) {
        this.articleTitle = articleTitle;
    }

    public StringFilter getArticleDoi() {
        return articleDoi;
    }

    public void setArticleDoi(StringFilter articleDoi) {
        this.articleDoi = articleDoi;
    }

    public StringFilter getArticleJournal() {
        return articleJournal;
    }

    public void setArticleJournal(StringFilter articleJournal) {
        this.articleJournal = articleJournal;
    }

    public LocalDateFilter getFetchDate() {
        return fetchDate;
    }

    public void setFetchDate(LocalDateFilter fetchDate) {
        this.fetchDate = fetchDate;
    }

    public StringFilter getCitation() {
        return citation;
    }

    public void setCitation(StringFilter citation) {
        this.citation = citation;
    }

    public ReviewStateFilter getReviewState() {
        return reviewState;
    }

    public void setReviewState(ReviewStateFilter reviewState) {
        this.reviewState = reviewState;
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
            Objects.equals(articleDoi, that.articleDoi) &&
            Objects.equals(articleJournal, that.articleJournal) &&
            Objects.equals(fetchDate, that.fetchDate) &&
            Objects.equals(citation, that.citation) &&
            Objects.equals(reviewState, that.reviewState) &&
            Objects.equals(revisionId, that.revisionId) &&
            Objects.equals(srepoId, that.srepoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        repoArticleId,
        repoDate,
        articleDate,
        articleTitle,
        articleDoi,
        articleJournal,
        fetchDate,
        citation,
        reviewState,
        revisionId,
        srepoId
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
                (articleDoi != null ? "articleDoi=" + articleDoi + ", " : "") +
                (articleJournal != null ? "articleJournal=" + articleJournal + ", " : "") +
                (fetchDate != null ? "fetchDate=" + fetchDate + ", " : "") +
                (citation != null ? "citation=" + citation + ", " : "") +
                (reviewState != null ? "reviewState=" + reviewState + ", " : "") +
                (revisionId != null ? "revisionId=" + revisionId + ", " : "") +
                (srepoId != null ? "srepoId=" + srepoId + ", " : "") +
            "}";
    }

}
