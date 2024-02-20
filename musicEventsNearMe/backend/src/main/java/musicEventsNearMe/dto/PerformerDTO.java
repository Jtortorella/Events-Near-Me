package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import musicEventsNearMe.interfaces.BaseEntity;

@Data
@Entity
@Table(name = "performers")
public class PerformerDTO implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;
    @JsonIgnore
    private String identifier;
    @Column(columnDefinition = "VARCHAR(500)")
    private String url;
    private String image;
    @JsonIgnore
    private String datePublished;
    @JsonIgnore
    private String dateModified;
    @JsonIgnore
    private LocalDateTime timeRecordWasEntered;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Genre> genres;

    @PrePersist
    private void beforePersist() {
        timeRecordWasEntered = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PerformerDTO that = (PerformerDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(url, that.url) &&
                Objects.equals(image, that.image) &&
                Objects.equals(datePublished, that.datePublished) &&
                Objects.equals(dateModified, that.dateModified) &&
                Objects.equals(genres, that.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}