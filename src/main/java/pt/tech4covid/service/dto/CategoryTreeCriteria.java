package pt.tech4covid.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pt.tech4covid.domain.CategoryTree} entity. This class is used
 * in {@link pt.tech4covid.web.rest.CategoryTreeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /category-trees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryTreeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemName;

    private BooleanFilter active;

    private LongFilter childId;

    private LongFilter newsletterId;

    private LongFilter revisionId;

    public CategoryTreeCriteria() {
    }

    public CategoryTreeCriteria(CategoryTreeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.childId = other.childId == null ? null : other.childId.copy();
        this.newsletterId = other.newsletterId == null ? null : other.newsletterId.copy();
        this.revisionId = other.revisionId == null ? null : other.revisionId.copy();
    }

    @Override
    public CategoryTreeCriteria copy() {
        return new CategoryTreeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getChildId() {
        return childId;
    }

    public void setChildId(LongFilter childId) {
        this.childId = childId;
    }

    public LongFilter getNewsletterId() {
        return newsletterId;
    }

    public void setNewsletterId(LongFilter newsletterId) {
        this.newsletterId = newsletterId;
    }

    public LongFilter getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(LongFilter revisionId) {
        this.revisionId = revisionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryTreeCriteria that = (CategoryTreeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(active, that.active) &&
            Objects.equals(childId, that.childId) &&
            Objects.equals(newsletterId, that.newsletterId) &&
            Objects.equals(revisionId, that.revisionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemName,
        active,
        childId,
        newsletterId,
        revisionId
        );
    }

    @Override
    public String toString() {
        return "CategoryTreeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (childId != null ? "childId=" + childId + ", " : "") +
                (newsletterId != null ? "newsletterId=" + newsletterId + ", " : "") +
                (revisionId != null ? "revisionId=" + revisionId + ", " : "") +
            "}";
    }

}
