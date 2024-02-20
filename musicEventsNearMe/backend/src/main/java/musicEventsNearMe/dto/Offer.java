package musicEventsNearMe.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long offer_id;
    private String name;
    @JsonIgnore
    private String identifier;
    @Column(columnDefinition = "VARCHAR(500)")
    private String url;
    @JsonIgnore
    private String datePublished;
    @JsonIgnore
    private String dateModified;
    private String category;
    private String validFrom;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private PriceSpecification priceSpecification;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Seller seller;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        Offer offer = (Offer) o;
        return Objects.equals(name, offer.name) &&
                Objects.equals(identifier, offer.identifier) &&
                Objects.equals(url, offer.url) &&
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
                offer_id, name, identifier, url, datePublished, dateModified,
                category, priceSpecification, seller, validFrom);
    }
}
