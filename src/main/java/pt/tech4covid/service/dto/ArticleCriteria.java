package pt.tech4covid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pt.tech4covid.domain.enumeration.ContentSource;
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
     * Class for filtering ContentSource
     */
    public static class ContentSourceFilter extends Filter<ContentSource> {

        public ContentSourceFilter() {
        }

        public ContentSourceFilter(ContentSourceFilter filter) {
            super(filter);
        }

        @Override
        public ContentSourceFilter copy() {
            return new ContentSourceFilter(this);
        }

    }
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

    private ContentSourceFilter fetchedFrom;

    private IntegerFilter sourceID;

    private LocalDateFilter sourceDate;

    private StringFilter sourceTitle;

    private StringFilter sourceAbstract;

    private LocalDateFilter importDate;

    private StringFilter outboundLink;

    private ReviewStateFilter reviewState;

    private LongFilter revisionId;

    private LongFilter pubNameId;

    public ArticleCriteria() {
    }

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fetchedFrom = other.fetchedFrom == null ? null : other.fetchedFrom.copy();
        this.sourceID = other.sourceID == null ? null : other.sourceID.copy();
        this.sourceDate = other.sourceDate == null ? null : other.sourceDate.copy();
        this.sourceTitle = other.sourceTitle == null ? null : other.sourceTitle.copy();
        this.sourceAbstract = other.sourceAbstract == null ? null : other.sourceAbstract.copy();
        this.importDate = other.importDate == null ? null : other.importDate.copy();
        this.outboundLink = other.outboundLink == null ? null : other.outboundLink.copy();
        this.reviewState = other.reviewState == null ? null : other.reviewState.copy();
        this.revisionId = other.revisionId == null ? null : other.revisionId.copy();
        this.pubNameId = other.pubNameId == null ? null : other.pubNameId.copy();
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

    public ContentSourceFilter getFetchedFrom() {
        return fetchedFrom;
    }

    public void setFetchedFrom(ContentSourceFilter fetchedFrom) {
        this.fetchedFrom = fetchedFrom;
    }

    public IntegerFilter getSourceID() {
        return sourceID;
    }

    public void setSourceID(IntegerFilter sourceID) {
        this.sourceID = sourceID;
    }

    public LocalDateFilter getSourceDate() {
        return sourceDate;
    }

    public void setSourceDate(LocalDateFilter sourceDate) {
        this.sourceDate = sourceDate;
    }

    public StringFilter getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(StringFilter sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public StringFilter getSourceAbstract() {
        return sourceAbstract;
    }

    public void setSourceAbstract(StringFilter sourceAbstract) {
        this.sourceAbstract = sourceAbstract;
    }

    public LocalDateFilter getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateFilter importDate) {
        this.importDate = importDate;
    }

    public StringFilter getOutboundLink() {
        return outboundLink;
    }

    public void setOutboundLink(StringFilter outboundLink) {
        this.outboundLink = outboundLink;
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

    public LongFilter getPubNameId() {
        return pubNameId;
    }

    public void setPubNameId(LongFilter pubNameId) {
        this.pubNameId = pubNameId;
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
            Objects.equals(fetchedFrom, that.fetchedFrom) &&
            Objects.equals(sourceID, that.sourceID) &&
            Objects.equals(sourceDate, that.sourceDate) &&
            Objects.equals(sourceTitle, that.sourceTitle) &&
            Objects.equals(sourceAbstract, that.sourceAbstract) &&
            Objects.equals(importDate, that.importDate) &&
            Objects.equals(outboundLink, that.outboundLink) &&
            Objects.equals(reviewState, that.reviewState) &&
            Objects.equals(revisionId, that.revisionId) &&
            Objects.equals(pubNameId, that.pubNameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fetchedFrom,
        sourceID,
        sourceDate,
        sourceTitle,
        sourceAbstract,
        importDate,
        outboundLink,
        reviewState,
        revisionId,
        pubNameId
        );
    }

    @Override
    public String toString() {
        return "ArticleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fetchedFrom != null ? "fetchedFrom=" + fetchedFrom + ", " : "") +
                (sourceID != null ? "sourceID=" + sourceID + ", " : "") +
                (sourceDate != null ? "sourceDate=" + sourceDate + ", " : "") +
                (sourceTitle != null ? "sourceTitle=" + sourceTitle + ", " : "") +
                (sourceAbstract != null ? "sourceAbstract=" + sourceAbstract + ", " : "") +
                (importDate != null ? "importDate=" + importDate + ", " : "") +
                (outboundLink != null ? "outboundLink=" + outboundLink + ", " : "") +
                (reviewState != null ? "reviewState=" + reviewState + ", " : "") +
                (revisionId != null ? "revisionId=" + revisionId + ", " : "") +
                (pubNameId != null ? "pubNameId=" + pubNameId + ", " : "") +
            "}";
    }

}
