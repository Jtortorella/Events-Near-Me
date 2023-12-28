package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
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
import musicEventsNearMe.dto.MusicEventDTO.ExternalIdentifier;
import musicEventsNearMe.interfaces.BaseEntity;

@Data
@Entity
@Table(name = "locations")
public class LocationDTO implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String identifier;
    private String url;
    private String image;

    private String datePublished;
    private String dateModified;
    private int maximumAttendeeCapacity;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GeoCoordinates geo;

    private List<Long> eventsId;

    private boolean isPermanentlyClosed;
    private int numUpcomingEvents;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExternalIdentifier> externalIdentifiers;

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
                Objects.equals(eventsId, that.eventsId) &&
                Objects.equals(isPermanentlyClosed, that.isPermanentlyClosed) &&
                Objects.equals(numUpcomingEvents, that.numUpcomingEvents) &&
                Objects.equals(externalIdentifiers, that.externalIdentifiers);
    }

    @Data
    @Entity
    @Table(name = "addresses")
    public static class Address {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String addressType;
        private String streetAddress;
        private String addressLocality;
        private String postalCode;

        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private AddressRegion addressRegion;

        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private AddressCountry addressCountry;

        private String streetAddress2;
        private String timezone;
        private int jamBaseMetroId;
        private int jamBaseCityId;
    }

    @Data
    @Entity
    @Table(name = "geo_coordinates")
    public static class GeoCoordinates {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String geoCoordinatesType;
        private double latitude;
        private double longitude;
    }

    @Data
    @Entity
    @Table(name = "address_countries")
    public static class AddressCountry {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String addressCountryType;
        private String identifier;
        private String name;
        private String alternateName;
    }

    @Data
    @Entity
    @Table(name = "address_regions")
    public static class AddressRegion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String addressRegionType;
        private String identifier;
        private String name;
        private String alternateName;
    }

    @Data
    @Entity
    @Table(name = "places")
    public static class Place {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String placeType;
        private String name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
