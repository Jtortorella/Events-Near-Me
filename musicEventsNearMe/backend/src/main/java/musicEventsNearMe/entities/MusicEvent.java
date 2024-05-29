package musicEventsNearMe.entities;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicEvent {

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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Location {
        private Long id;
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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Offer {
        private Long id;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UrlType {
        private Long id;
        private String urlType;
        private String identifier;
        private String url;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExternalIdentifier {
        private Long id;
        private String source;
        private String identifier;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeoCoordinates {
        private Long id;
        private String geoCoordinatesType;
        private double latitude;
        private double longitude;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private Long id;
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
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressRegion {
        private Long id;
        private String addressRegionType;
        private String identifier;
        private String name;
        private String alternateName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressCountry {
        private Long id;
        private String addressCountryType;
        private String identifier;
        private String name;
        private String alternateName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Performer {
        private Long id;
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
        private List<String> genre;
        private List<MusicEvent> events;
        private String bandOrMusician;
        private int numUpcomingEvents;
        private String performanceDate;
        private int performanceRank;
        private boolean isHeadliner;
        private boolean dateIsConfirmed;
    }

    List<String> getGenre(Performer performer) {
        return performer.getGenre();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceSpecification {
        private Long id;
        private String priceType;
        private String minPrice;
        private String maxPrice;
        private String price;
        private String priceCurrency;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Seller {
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

    public List<Performer> getPerformer() {
        // Assuming performer has a method getGenres() that returns a List<String>
        return performer != null ? performer : null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Place {
        private Long id;
        private String placeType;
        private String name;
    }
}
