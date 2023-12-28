package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import musicEventsNearMe.interfaces.BaseEntity;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "music_events")
public class MusicEventDTO implements BaseEntity {

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String previousStartDate;
    private String doorTime;

    private Long locationId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Offer> offers;

    private List<Long> performersId;

    private String eventAttendanceMode;
    private boolean isAccessibleForFree;
    private String promoImage;
    private String eventType;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> streamIds;

    private boolean headlinerInSupport;
    private String customTitle;
    private String subtitle;
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
                Objects.equals(previousStartDate, that.previousStartDate) &&
                Objects.equals(doorTime, that.doorTime) &&
                Objects.equals(locationId, that.locationId) &&
                Objects.equals(offers, that.offers) &&
                Objects.equals(performersId, that.performersId) &&
                Objects.equals(eventAttendanceMode, that.eventAttendanceMode) &&
                Objects.equals(isAccessibleForFree, that.isAccessibleForFree) &&
                Objects.equals(promoImage, that.promoImage) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(streamIds, that.streamIds) &&
                Objects.equals(headlinerInSupport, that.headlinerInSupport) &&
                Objects.equals(customTitle, that.customTitle) &&
                Objects.equals(subtitle, that.subtitle);
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

        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private PriceSpecification priceSpecification;

        @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}