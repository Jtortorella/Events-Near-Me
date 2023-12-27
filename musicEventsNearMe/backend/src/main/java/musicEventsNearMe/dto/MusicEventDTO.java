package musicEventsNearMe.dto;

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
import musicEventsNearMe.Interfaces.BaseEntity;

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
    private String startDate;
    private String endDate;
    private String previousStartDate;
    private String doorTime;

    private Long locationId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Offer> offers;

    private List<Long> performersId;

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

}