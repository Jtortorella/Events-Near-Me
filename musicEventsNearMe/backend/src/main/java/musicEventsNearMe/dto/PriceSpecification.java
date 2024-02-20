package musicEventsNearMe.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "price_specifications")
public class PriceSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    private String price;
    private String priceCurrency;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PriceSpecification that = (PriceSpecification) o;
        return that.price.equals(price) &&
                Objects.equals(priceCurrency, that.priceCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, priceCurrency);
    }
}
