package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import musicEventsNearMe.dto.LocationDTO.Place;
import musicEventsNearMe.dto.MusicEventDTO.ExternalIdentifier;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PerformerDTO> member;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PerformerDTO> memberOf;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Place foundingLocation;

    private String foundingDate;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> genre;

    private List<Long> eventsId;

    private String bandOrMusician;
    private int numUpcomingEvents;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExternalIdentifier> externalIdentifiers;

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
                Objects.equals(foundingLocation, that.foundingLocation) &&
                Objects.equals(foundingDate, that.foundingDate) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(eventsId, that.eventsId) &&
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