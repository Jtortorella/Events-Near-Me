package musicEventsNearMe.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class MusicEvent {

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

    private Location location;

    private List<Offer> offers;

    private List<Performer> performer;

    private String eventAttendanceMode;
    private boolean isAccessibleForFree;
    private String promoImage;
    private String eventType;

    private List<String> streamIds;

    private boolean headlinerInSupport;
    private String customTitle;
    private String subtitle;
    private LocalDateTime timeRecordWasEntered;

    @Data

    public static class Location {

        private String name;
        private String identifier;
        private String url;
        private String image;

        private String datePublished;
        private String dateModified;
        private int maximumAttendeeCapacity;

        private Address address;

        private GeoCoordinates geo;

        private List<MusicEvent> events;

        private boolean isPermanentlyClosed;
        private int numUpcomingEvents;

        private List<ExternalIdentifier> externalIdentifiers;

    }

    @Data
    public static class Offer {

        private String name;
        private String identifier;
        private String url;
        private String image;

        private String datePublished;
        private String dateModified;
        private String category;

        private PriceSpecification priceSpecification;

        private Seller seller;

        private String validFrom;
    }

    @Data

    public static class UrlType {

        private String urlType;
        private String identifier;
        private String url;
    }

    @Data

    public static class Address {

        private String addressType;
        private String streetAddress;
        private String addressLocality;
        private String postalCode;

        private AddressRegion addressRegion;

        private AddressCountry addressCountry;

        private String streetAddress2;
        private String timezone;
        private int jamBaseMetroId;
        private int jamBaseCityId;
    }

    @Data
    public static class ExternalIdentifier {

        private String source;

        private String identifier;
    }

    @Data
    public static class GeoCoordinates {

        private String geoCoordinatesType;
        private double latitude;
        private double longitude;
    }

    @Data

    public static class Performer {

        private String name;
        private String identifier;
        private String url;
        private String image;

        private String datePublished;
        private String dateModified;
        private String performerType;

        private List<Performer> member;

        private List<Performer> memberOf;

        private Place foundingLocation;

        private String foundingDate;

        @ElementCollection
        private List<String> genre;

        private List<MusicEvent> events;

        private String bandOrMusician;
        private int numUpcomingEvents;

        private List<ExternalIdentifier> externalIdentifiers;

        private String performanceDate;
        private int performanceRank;
        private boolean isHeadliner;
        private boolean dateIsConfirmed;
    }

    @Data
    public static class PriceSpecification {

        private String priceType;
        private double minPrice;
        private double maxPrice;
        private double price;
        private String priceCurrency;
    }

    @Data
    public static class Seller {

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
    public static class Place {

        private String placeType;
        private String name;
    }

    @Data
    public static class AddressRegion {

        private String addressRegionType;
        private String identifier;
        private String name;
        private String alternateName;
    }

    @Data
    public static class AddressCountry {

        private String addressCountryType;
        private String identifier;
        private String name;
        private String alternateName;
    }
}
