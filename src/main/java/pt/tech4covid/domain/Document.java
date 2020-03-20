package pt.tech4covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "abstract_desc")
    private String abstractDesc;

    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Column(name = "source")
    private String source;

    @Column(name = "link")
    private String link;

    @OneToMany(mappedBy = "document")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Revision> revisions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("names")
    private Topic topic;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Document title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public Document date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAbstractDesc() {
        return abstractDesc;
    }

    public Document abstractDesc(String abstractDesc) {
        this.abstractDesc = abstractDesc;
        return this;
    }

    public void setAbstractDesc(String abstractDesc) {
        this.abstractDesc = abstractDesc;
    }

    public String getKeywords() {
        return keywords;
    }

    public Document keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSource() {
        return source;
    }

    public Document source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public Document link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Revision> getRevisions() {
        return revisions;
    }

    public Document revisions(Set<Revision> revisions) {
        this.revisions = revisions;
        return this;
    }

    public Document addRevision(Revision revision) {
        this.revisions.add(revision);
        revision.setDocument(this);
        return this;
    }

    public Document removeRevision(Revision revision) {
        this.revisions.remove(revision);
        revision.setDocument(null);
        return this;
    }

    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }

    public Topic getTopic() {
        return topic;
    }

    public Document topic(Topic topic) {
        this.topic = topic;
        return this;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", date='" + getDate() + "'" +
            ", abstractDesc='" + getAbstractDesc() + "'" +
            ", keywords='" + getKeywords() + "'" +
            ", source='" + getSource() + "'" +
            ", link='" + getLink() + "'" +
            "}";
    }
}
