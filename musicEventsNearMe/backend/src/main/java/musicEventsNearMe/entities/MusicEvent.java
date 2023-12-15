package musicEventsNearMe.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "music_events")
public class MusicEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String identifier;
    private String url;
    private String image;

    private String datePublished;
    private String dateModified;
    private String eventStatus;
    private String startDate;
    private String endDate;
    private String previousStartDate;
    private String doorTime;

    @ManyToOne(cascade = CascadeType.ALL)
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Offer> offers;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Performer> performer;

    private String eventAttendanceMode;
    private boolean isAccessibleForFree;
    private String promoImage;
    private String eventType;

    @ElementCollection
    private List<String> streamIds;

    private boolean headlinerInSupport;
    private String customTitle;
    private String subtitle;
    private LocalDateTime timeRecordWasEntered;

    @PrePersist
    private void beforePersist() {
        timeRecordWasEntered = LocalDateTime.now();
    }

    @Data
    @Entity
    @Table(name = "locations")
    public static class Location {
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

        @OneToMany(cascade = CascadeType.ALL)
        private List<MusicEvent> events;

        private boolean isPermanentlyClosed;
        private int numUpcomingEvents;

        @OneToMany(cascade = CascadeType.ALL)
        private List<ExternalIdentifier> externalIdentifiers;

    }

    @Data
    @Entity
    @Table(name = "offers")
    public static class Offer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String identifier;
        private String url;
        private String image;

        private String datePublished;
        private String dateModified;
        private String category;

        @ManyToOne(cascade = CascadeType.ALL)
        private PriceSpecification priceSpecification;

        @ManyToOne(cascade = CascadeType.ALL)
        private Seller seller;

        private String validFrom;
    }

    @Data
    @Entity
    @Table(name = "url_types")
    public static class UrlType {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String urlType;
        private String identifier;
        private String url;
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
    @Table(name = "external_identifiers")
    public static class ExternalIdentifier {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(columnDefinition = "VARCHAR(255)") // Adjust the column definition as needed
        private String source;
        @Column(columnDefinition = "VARCHAR(255)") // Adjust the column definition as needed

        private String identifier;
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
    @Table(name = "performers")
    public static class Performer {
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

        @OneToMany(cascade = CascadeType.ALL)
        private List<Performer> member;

        @OneToMany(cascade = CascadeType.ALL)
        private List<Performer> memberOf;

        @ManyToOne(cascade = CascadeType.ALL)
        private Place foundingLocation;

        private String foundingDate;

        @ElementCollection
        private List<String> genre;

        @OneToMany(cascade = CascadeType.ALL)
        private List<MusicEvent> events;

        private String bandOrMusician;
        private int numUpcomingEvents;

        @OneToMany(cascade = CascadeType.ALL)
        private List<ExternalIdentifier> externalIdentifiers;

        private String performanceDate;
        private int performanceRank;
        private boolean isHeadliner;
        private boolean dateIsConfirmed;
    }

    @Data
    @Entity
    @Table(name = "price_specifications")
    public static class PriceSpecification {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String priceType;
        private double minPrice;
        private double maxPrice;
        private double price;
        private String priceCurrency;
    }

    @Data
    @Entity
    @Table(name = "sellers")
    public static class Seller {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String sellerType;
        private String identifier;
        private String disambiguatingDescription;
        private String name;
        private String url;
        private String image;

        private String datePublished;
        private String dateModified;
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
}
