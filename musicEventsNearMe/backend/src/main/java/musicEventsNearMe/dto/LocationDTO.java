package musicEventsNearMe.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import musicEventsNearMe.dto.MusicEventDTO.ExternalIdentifier;

@Data
@Entity
@Table(name = "locations")
public class LocationDTO {
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

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    @ManyToOne(cascade = CascadeType.ALL)
    private GeoCoordinates geo;

    private List<Long> eventsId;

    private boolean isPermanentlyClosed;
    private int numUpcomingEvents;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalIdentifier> externalIdentifiers;

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

        @ManyToOne(cascade = CascadeType.ALL)
        private AddressRegion addressRegion;

        @ManyToOne(cascade = CascadeType.ALL)
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
}
