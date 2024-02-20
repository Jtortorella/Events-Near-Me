package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import musicEventsNearMe.interfaces.BaseEntity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "music_events")
public class MusicEventDTO implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "Music_Event_ID")
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
    private String eventStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String doorTime;
    private String eventAttendanceMode;
    private boolean isAccessibleForFree;
    private String promoImage;
    private String eventType;
    @JsonIgnore
    private LocalDateTime timeRecordWasEntered;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Location_ID")
    private LocationDTO location;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Offer> offers;

    @JsonIgnoreProperties("musicEvents")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<PerformerDTO> performer;

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
        MusicEventDTO that = (MusicEventDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(url, that.url) &&
                Objects.equals(image, that.image) &&
                Objects.equals(datePublished, that.datePublished) &&
                Objects.equals(dateModified, that.dateModified) &&
                Objects.equals(eventStatus, that.eventStatus) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(doorTime, that.doorTime) &&
                Objects.equals(eventAttendanceMode, that.eventAttendanceMode) &&
                Objects.equals(isAccessibleForFree, that.isAccessibleForFree) &&
                Objects.equals(promoImage, that.promoImage) &&
                Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, identifier, url, image, datePublished, dateModified,
                eventStatus, startDate, endDate, doorTime,
                offers, eventAttendanceMode, isAccessibleForFree,
                promoImage, eventType);
    }

}
