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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link pt.tech4covid.domain.Newsletter} entity. This class is used
 * in {@link pt.tech4covid.web.rest.NewsletterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /newsletters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NewsletterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private LocalDateFilter registrationDate;

    private BooleanFilter rgpdAuth;

    private LongFilter categoryTreeId;

    public NewsletterCriteria() {
    }

    public NewsletterCriteria(NewsletterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.registrationDate = other.registrationDate == null ? null : other.registrationDate.copy();
        this.rgpdAuth = other.rgpdAuth == null ? null : other.rgpdAuth.copy();
        this.categoryTreeId = other.categoryTreeId == null ? null : other.categoryTreeId.copy();
    }

    @Override
    public NewsletterCriteria copy() {
        return new NewsletterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LocalDateFilter getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateFilter registrationDate) {
        this.registrationDate = registrationDate;
    }

    public BooleanFilter getRgpdAuth() {
        return rgpdAuth;
    }

    public void setRgpdAuth(BooleanFilter rgpdAuth) {
        this.rgpdAuth = rgpdAuth;
    }

    public LongFilter getCategoryTreeId() {
        return categoryTreeId;
    }

    public void setCategoryTreeId(LongFilter categoryTreeId) {
        this.categoryTreeId = categoryTreeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NewsletterCriteria that = (NewsletterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(registrationDate, that.registrationDate) &&
            Objects.equals(rgpdAuth, that.rgpdAuth) &&
            Objects.equals(categoryTreeId, that.categoryTreeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        email,
        registrationDate,
        rgpdAuth,
        categoryTreeId
        );
    }

    @Override
    public String toString() {
        return "NewsletterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (registrationDate != null ? "registrationDate=" + registrationDate + ", " : "") +
                (rgpdAuth != null ? "rgpdAuth=" + rgpdAuth + ", " : "") +
                (categoryTreeId != null ? "categoryTreeId=" + categoryTreeId + ", " : "") +
            "}";
    }

}
