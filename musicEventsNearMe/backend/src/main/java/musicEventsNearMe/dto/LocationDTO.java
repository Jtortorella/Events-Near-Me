package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JsonIgnore
    @Column(name = "Location_ID")
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
    private int maximumAttendeeCapacity;
    private boolean isPermanentlyClosed;
    private int numUpcomingEvents;
    @JsonIgnore
    private LocalDateTime timeRecordWasEntered;

    @ManyToOne
    @JsonIgnoreProperties("locations")
    @JoinColumn(name = "Address_ID")
    private Address address;

    @ManyToOne
    @JsonIgnoreProperties("location")
    @JoinColumn(name = "Geo_ID")
    private GeoCoordinate geo;

    @PrePersist
    private void beforePersist() {
        timeRecordWasEntered = LocalDateTime.now();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identifier, url, image, datePublished, dateModified,
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
}
