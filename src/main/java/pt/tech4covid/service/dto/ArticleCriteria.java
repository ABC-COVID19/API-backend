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

    private IntegerFilter sourceID;

    private LocalDateFilter sourceDate;

    private StringFilter sourceTitle;

    private StringFilter sourceAbstract;

    private LocalDateFilter pubmedDate;

    private LocalDateFilter officialPubDate;

    private StringFilter doi;

    private StringFilter journal;

    private StringFilter citation;

    private ReviewStateFilter reviewState;

    private LongFilter revisionId;

    private LongFilter cntsourceId;

    public ArticleCriteria() {
    }

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sourceID = other.sourceID == null ? null : other.sourceID.copy();
        this.sourceDate = other.sourceDate == null ? null : other.sourceDate.copy();
        this.sourceTitle = other.sourceTitle == null ? null : other.sourceTitle.copy();
        this.sourceAbstract = other.sourceAbstract == null ? null : other.sourceAbstract.copy();
        this.pubmedDate = other.pubmedDate == null ? null : other.pubmedDate.copy();
        this.officialPubDate = other.officialPubDate == null ? null : other.officialPubDate.copy();
        this.doi = other.doi == null ? null : other.doi.copy();
        this.journal = other.journal == null ? null : other.journal.copy();
        this.citation = other.citation == null ? null : other.citation.copy();
        this.reviewState = other.reviewState == null ? null : other.reviewState.copy();
        this.revisionId = other.revisionId == null ? null : other.revisionId.copy();
        this.cntsourceId = other.cntsourceId == null ? null : other.cntsourceId.copy();
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

    public LocalDateFilter getPubmedDate() {
        return pubmedDate;
    }

    public void setPubmedDate(LocalDateFilter pubmedDate) {
        this.pubmedDate = pubmedDate;
    }

    public LocalDateFilter getOfficialPubDate() {
        return officialPubDate;
    }

    public void setOfficialPubDate(LocalDateFilter officialPubDate) {
        this.officialPubDate = officialPubDate;
    }

    public StringFilter getDoi() {
        return doi;
    }

    public void setDoi(StringFilter doi) {
        this.doi = doi;
    }

    public StringFilter getJournal() {
        return journal;
    }

    public void setJournal(StringFilter journal) {
        this.journal = journal;
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

    public LongFilter getCntsourceId() {
        return cntsourceId;
    }

    public void setCntsourceId(LongFilter cntsourceId) {
        this.cntsourceId = cntsourceId;
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
            Objects.equals(sourceID, that.sourceID) &&
            Objects.equals(sourceDate, that.sourceDate) &&
            Objects.equals(sourceTitle, that.sourceTitle) &&
            Objects.equals(sourceAbstract, that.sourceAbstract) &&
            Objects.equals(pubmedDate, that.pubmedDate) &&
            Objects.equals(officialPubDate, that.officialPubDate) &&
            Objects.equals(doi, that.doi) &&
            Objects.equals(journal, that.journal) &&
            Objects.equals(citation, that.citation) &&
            Objects.equals(reviewState, that.reviewState) &&
            Objects.equals(revisionId, that.revisionId) &&
            Objects.equals(cntsourceId, that.cntsourceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sourceID,
        sourceDate,
        sourceTitle,
        sourceAbstract,
        pubmedDate,
        officialPubDate,
        doi,
        journal,
        citation,
        reviewState,
        revisionId,
        cntsourceId
        );
    }

    @Override
    public String toString() {
        return "ArticleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sourceID != null ? "sourceID=" + sourceID + ", " : "") +
                (sourceDate != null ? "sourceDate=" + sourceDate + ", " : "") +
                (sourceTitle != null ? "sourceTitle=" + sourceTitle + ", " : "") +
                (sourceAbstract != null ? "sourceAbstract=" + sourceAbstract + ", " : "") +
                (pubmedDate != null ? "pubmedDate=" + pubmedDate + ", " : "") +
                (officialPubDate != null ? "officialPubDate=" + officialPubDate + ", " : "") +
                (doi != null ? "doi=" + doi + ", " : "") +
                (journal != null ? "journal=" + journal + ", " : "") +
                (citation != null ? "citation=" + citation + ", " : "") +
                (reviewState != null ? "reviewState=" + reviewState + ", " : "") +
                (revisionId != null ? "revisionId=" + revisionId + ", " : "") +
                (cntsourceId != null ? "cntsourceId=" + cntsourceId + ", " : "") +
            "}";
    }

}
