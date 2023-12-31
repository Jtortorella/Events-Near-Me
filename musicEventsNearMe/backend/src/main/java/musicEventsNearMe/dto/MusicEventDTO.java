package musicEventsNearMe.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
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
                Objects.equals(eventAttendanceMode, that.eventAttendanceMode) &&
                Objects.equals(isAccessibleForFree, that.isAccessibleForFree) &&
                Objects.equals(promoImage, that.promoImage) &&
                Objects.equals(eventType, that.eventType) &&
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            Offer offer = (Offer) o;
            return Objects.equals(name, offer.name) &&
                    Objects.equals(identifier, offer.identifier) &&
                    Objects.equals(url, offer.url) &&
                    Objects.equals(image, offer.image) &&
                    Objects.equals(datePublished, offer.datePublished) &&
                    Objects.equals(dateModified, offer.dateModified) &&
                    Objects.equals(category, offer.category) &&
                    Objects.equals(priceSpecification, offer.priceSpecification) &&
                    Objects.equals(seller, offer.seller) &&
                    Objects.equals(validFrom, offer.validFrom);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    id, name, identifier, url, image, datePublished, dateModified,
                    category, priceSpecification, seller, validFrom);
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            PriceSpecification that = (PriceSpecification) o;
            return Objects.equals(priceType, that.priceType) &&
                    Double.compare(that.minPrice, minPrice) == 0 &&
                    Double.compare(that.maxPrice, maxPrice) == 0 &&
                    Double.compare(that.price, price) == 0 &&
                    Objects.equals(priceCurrency, that.priceCurrency);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, priceType, minPrice, maxPrice, price, priceCurrency);
        }
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            // Check if the object is an instance of Seller
            if (!(o instanceof Seller)) {
                return false;
            }

            Seller seller = (Seller) o;

            return Objects.equals(sellerType, seller.sellerType) &&
                    Objects.equals(identifier, seller.identifier) &&
                    Objects.equals(disambiguatingDescription, seller.disambiguatingDescription) &&
                    Objects.equals(name, seller.name) &&
                    Objects.equals(url, seller.url) &&
                    Objects.equals(image, seller.image) &&
                    Objects.equals(datePublished, seller.datePublished) &&
                    Objects.equals(dateModified, seller.dateModified);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, sellerType, identifier, disambiguatingDescription, name, url, image, datePublished,
                    dateModified);
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, identifier, url, image, datePublished, dateModified,
                eventStatus, startDate, endDate, previousStartDate, doorTime,
                locationId, offers, eventAttendanceMode, isAccessibleForFree,
                promoImage, eventType, streamIds, headlinerInSupport, customTitle,
                subtitle);
    }

}