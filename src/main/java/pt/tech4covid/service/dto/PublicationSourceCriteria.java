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
 * Criteria class for the {@link pt.tech4covid.domain.PublicationSource} entity. This class is used
 * in {@link pt.tech4covid.web.rest.PublicationSourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /publication-sources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PublicationSourceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sourceName;

    private BooleanFilter active;

    private LongFilter nameId;

    public PublicationSourceCriteria() {
    }

    public PublicationSourceCriteria(PublicationSourceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sourceName = other.sourceName == null ? null : other.sourceName.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.nameId = other.nameId == null ? null : other.nameId.copy();
    }

    @Override
    public PublicationSourceCriteria copy() {
        return new PublicationSourceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSourceName() {
        return sourceName;
    }

    public void setSourceName(StringFilter sourceName) {
        this.sourceName = sourceName;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getNameId() {
        return nameId;
    }

    public void setNameId(LongFilter nameId) {
        this.nameId = nameId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PublicationSourceCriteria that = (PublicationSourceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sourceName, that.sourceName) &&
            Objects.equals(active, that.active) &&
            Objects.equals(nameId, that.nameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sourceName,
        active,
        nameId
        );
    }

    @Override
    public String toString() {
        return "PublicationSourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sourceName != null ? "sourceName=" + sourceName + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (nameId != null ? "nameId=" + nameId + ", " : "") +
            "}";
    }

}
