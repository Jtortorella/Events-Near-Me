package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import musicEventsNearMe.interfaces.BaseEntity;

@Data
@Entity
@Table(name = "locations")
public class LocationDTO implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    private String name;
    private String identifier;
    private String url;
    private String image;

    private String datePublished;
    private String dateModified;
    private int maximumAttendeeCapacity;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    private Address address;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location", orphanRemoval = true)
    private GeoCoordinate geo;

    private boolean isPermanentlyClosed;
    private int numUpcomingEvents;

    private LocalDateTime timeRecordWasEntered;

    @PrePersist
    private void beforePersist() {
        timeRecordWasEntered = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(location_id, name, identifier, url, image, datePublished, dateModified,
                maximumAttendeeCapacity, isPermanentlyClosed, numUpcomingEvents,
                timeRecordWasEntered);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LocationDTO that = (LocationDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(identifier, that.identifier) &&
                Objects.equals(url, that.url) &&
                Objects.equals(image, that.image) &&
                Objects.equals(datePublished, that.datePublished) &&
                Objects.equals(dateModified, that.dateModified) &&
                Objects.equals(maximumAttendeeCapacity, that.maximumAttendeeCapacity) &&
                Objects.equals(address, that.address) &&
                Objects.equals(geo, that.geo) &&
                Objects.equals(isPermanentlyClosed, that.isPermanentlyClosed) &&
                Objects.equals(numUpcomingEvents, that.numUpcomingEvents);
    }

    @Override
    public Long getId() {
        return this.location_id;
    }

    @Override
    public void setId(Long id) {
        setLocation_id(id);
    }
}
