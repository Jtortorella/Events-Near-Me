package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private Long id;

    private String name;
    private String identifier;
    private String url;
    private String image;

    private String datePublished;
    private String dateModified;
    private String performerType;

    @ManyToMany
    private List<Genre> genres;

    private String bandOrMusician;
    private int numUpcomingEvents;

    private String performanceDate;
    private int performanceRank;
    private boolean isHeadliner;
    private boolean dateIsConfirmed;

    private LocalDateTime timeRecordWasEntered;

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
                Objects.equals(performerType, that.performerType) &&
                Objects.equals(genres, that.genres) &&
                Objects.equals(bandOrMusician, that.bandOrMusician) &&
                Objects.equals(numUpcomingEvents, that.numUpcomingEvents) &&
                Objects.equals(performanceDate, that.performanceDate) &&
                Objects.equals(performanceRank, that.performanceRank) &&
                Objects.equals(isHeadliner, that.isHeadliner) &&
                Objects.equals(dateIsConfirmed, that.dateIsConfirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}