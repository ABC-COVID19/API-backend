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

/**
 * Criteria class for the {@link pt.tech4covid.domain.Revision} entity. This class is used
 * in {@link pt.tech4covid.web.rest.RevisionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /revisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RevisionCriteria implements Serializable, Criteria {
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

    private StringFilter title;

    private StringFilter summary;

    private StringFilter reviewer;

    private BooleanFilter active;

    private ReviewStateFilter reviewState;

    private StringFilter returnNotes;

    private BooleanFilter reviewedByPeer;

    private IntegerFilter communityVotes;

    private LongFilter articleId;

    private LongFilter typeId;

    private LongFilter areaId;

    public RevisionCriteria() {
    }

    public RevisionCriteria(RevisionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.summary = other.summary == null ? null : other.summary.copy();
        this.reviewer = other.reviewer == null ? null : other.reviewer.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.reviewState = other.reviewState == null ? null : other.reviewState.copy();
        this.returnNotes = other.returnNotes == null ? null : other.returnNotes.copy();
        this.reviewedByPeer = other.reviewedByPeer == null ? null : other.reviewedByPeer.copy();
        this.communityVotes = other.communityVotes == null ? null : other.communityVotes.copy();
        this.articleId = other.articleId == null ? null : other.articleId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.areaId = other.areaId == null ? null : other.areaId.copy();
    }

    @Override
    public RevisionCriteria copy() {
        return new RevisionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSummary() {
        return summary;
    }

    public void setSummary(StringFilter summary) {
        this.summary = summary;
    }

    public StringFilter getReviewer() {
        return reviewer;
    }

    public void setReviewer(StringFilter reviewer) {
        this.reviewer = reviewer;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public ReviewStateFilter getReviewState() {
        return reviewState;
    }

    public void setReviewState(ReviewStateFilter reviewState) {
        this.reviewState = reviewState;
    }

    public StringFilter getReturnNotes() {
        return returnNotes;
    }

    public void setReturnNotes(StringFilter returnNotes) {
        this.returnNotes = returnNotes;
    }

    public BooleanFilter getReviewedByPeer() {
        return reviewedByPeer;
    }

    public void setReviewedByPeer(BooleanFilter reviewedByPeer) {
        this.reviewedByPeer = reviewedByPeer;
    }

    public IntegerFilter getCommunityVotes() {
        return communityVotes;
    }

    public void setCommunityVotes(IntegerFilter communityVotes) {
        this.communityVotes = communityVotes;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getAreaId() {
        return areaId;
    }

    public void setAreaId(LongFilter areaId) {
        this.areaId = areaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RevisionCriteria that = (RevisionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(summary, that.summary) &&
            Objects.equals(reviewer, that.reviewer) &&
            Objects.equals(active, that.active) &&
            Objects.equals(reviewState, that.reviewState) &&
            Objects.equals(returnNotes, that.returnNotes) &&
            Objects.equals(reviewedByPeer, that.reviewedByPeer) &&
            Objects.equals(communityVotes, that.communityVotes) &&
            Objects.equals(articleId, that.articleId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(areaId, that.areaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        summary,
        reviewer,
        active,
        reviewState,
        returnNotes,
        reviewedByPeer,
        communityVotes,
        articleId,
        typeId,
        areaId
        );
    }

    @Override
    public String toString() {
        return "RevisionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (summary != null ? "summary=" + summary + ", " : "") +
                (reviewer != null ? "reviewer=" + reviewer + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (reviewState != null ? "reviewState=" + reviewState + ", " : "") +
                (returnNotes != null ? "returnNotes=" + returnNotes + ", " : "") +
                (reviewedByPeer != null ? "reviewedByPeer=" + reviewedByPeer + ", " : "") +
                (communityVotes != null ? "communityVotes=" + communityVotes + ", " : "") +
                (articleId != null ? "articleId=" + articleId + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (areaId != null ? "areaId=" + areaId + ", " : "") +
            "}";
    }

}
