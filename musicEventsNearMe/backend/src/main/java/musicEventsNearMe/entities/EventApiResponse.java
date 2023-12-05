package musicEventsNearMe.entities;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class EventApiResponse {
    private boolean success;
    private Pagination pagination;
    private List<Event> events;
    private Request request;

    @Data
    @ToString
    public static class Pagination {
        private int page;
        private int perPage;
        private int totalItems;
        private int totalPages;
        private String nextPage;
        private String previousPage;
    }

    @Data
    @ToString
    public static class Event {
        private String name;
        private String identifier;
        private String url;
        private String image;
        private List<UrlType> sameAs;
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
        private String type;
        private List<String> streamIds;
        private boolean headlinerInSupport;
        private String customTitle;
        private String subtitle;

        @Data
        @ToString
        public static class Location {
            private String name;
            private String identifier;
            private String url;
            private String image;
            private List<UrlType> sameAs;
            private String datePublished;
            private String dateModified;
            private int maximumAttendeeCapacity;
            private Address address;
            private GeoCoordinates geo;
            private List<Event> events;
            private boolean isPermanentlyClosed;
            private int numUpcomingEvents;
            private List<ExternalIdentifier> externalIdentifiers;
        }

        @Data
        @ToString
        public static class Address {
            private String type;
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
        @ToString
        public static class AddressRegion {
            private String type;
            private String identifier;
            private String name;
            private String alternateName;
        }

        @Data
        @ToString
        public static class AddressCountry {
            private String type;
            private String identifier;
            private String name;
            private String alternateName;
        }

        @Data
        @ToString
        public static class GeoCoordinates {
            private String type;
            private double latitude;
            private double longitude;
        }

        @Data
        @ToString
        public static class Offer {
            private String name;
            private String identifier;
            private String url;
            private String image;
            private List<UrlType> sameAs;
            private String datePublished;
            private String dateModified;
            private String category;
            private PriceSpecification priceSpecification;
            private Seller seller;
            private String validFrom;
        }

        @Data
        @ToString
        public static class PriceSpecification {
            private String type;
            private double minPrice;
            private double maxPrice;
            private double price;
            private String priceCurrency;
        }

        @Data
        @ToString
        public static class Seller {
            private String type;
            private String identifier;
            private String disambiguatingDescription;
            private String name;
            private String url;
            private String image;
            private List<UrlType> sameAs;
            private String datePublished;
            private String dateModified;
        }

        @Data
        @ToString
        public static class Performer {
            private String name;
            private String identifier;
            private String url;
            private String image;
            private List<UrlType> sameAs;
            private String datePublished;
            private String dateModified;
            private String type;
            private List<Performer> member;
            private List<Performer> memberOf;
            private Place foundingLocation;
            private String foundingDate;
            private List<String> genre;
            private List<Event> events;
            private String bandOrMusician;
            private int numUpcomingEvents;
            private List<ExternalIdentifier> externalIdentifiers;
            private String performanceDate;
            private int performanceRank;
            private boolean isHeadliner;
            private boolean dateIsConfirmed;
        }

        @Data
        @ToString
        public static class Place {
            private String type;
            private String name;
        }

        @Data
        @ToString
        public static class ExternalIdentifier {
            private String source;
            private String identifier;
        }
    }

    @Data
    @ToString
    public static class Request {
        private String endpoint;
        private String method;
        private Object params;
        private String ip;
        private String userAgent;
    }

    @Data
    @ToString
    public static class UrlType {
        private String type;
        private String identifier;
        private String url;
    }
}
