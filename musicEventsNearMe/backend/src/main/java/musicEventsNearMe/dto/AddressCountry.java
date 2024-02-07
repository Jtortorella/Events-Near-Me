package musicEventsNearMe.dto;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address_countries")
public class AddressCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long address_country_id;
    private String identifier;
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AddressCountry that = (AddressCountry) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name);
    }
}
