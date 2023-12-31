package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    private boolean isPermanentlyClosed;
    private int numUpcomingEvents;

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
                Objects.equals(isPermanentlyClosed, that.isPermanentlyClosed) &&
                Objects.equals(numUpcomingEvents, that.numUpcomingEvents);
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Address address = (Address) o;
            return Objects.equals(addressType, address.addressType) &&
                    Objects.equals(streetAddress, address.streetAddress) &&
                    Objects.equals(addressLocality, address.addressLocality) &&
                    Objects.equals(postalCode, address.postalCode) &&
                    Objects.equals(addressRegion, address.addressRegion) &&
                    Objects.equals(addressCountry, address.addressCountry) &&
                    Objects.equals(streetAddress2, address.streetAddress2) &&
                    jamBaseMetroId == address.jamBaseMetroId &&
                    jamBaseCityId == address.jamBaseCityId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(addressType, streetAddress, addressLocality, postalCode,
                    addressRegion, addressCountry, streetAddress2, timezone, jamBaseMetroId, jamBaseCityId);
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            GeoCoordinates that = (GeoCoordinates) o;
            return Objects.equals(geoCoordinatesType, that.geoCoordinatesType) &&
                    Double.compare(that.latitude, latitude) == 0 &&
                    Double.compare(that.longitude, longitude) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(geoCoordinatesType, latitude, longitude);
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            AddressCountry that = (AddressCountry) o;
            return Objects.equals(addressCountryType, that.addressCountryType) &&
                    Objects.equals(identifier, that.identifier) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(alternateName, that.alternateName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(addressCountryType, identifier, name, alternateName);
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            AddressRegion that = (AddressRegion) o;
            return Objects.equals(addressRegionType, that.addressRegionType) &&
                    Objects.equals(identifier, that.identifier) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(alternateName, that.alternateName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(addressRegionType, identifier, name, alternateName);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
