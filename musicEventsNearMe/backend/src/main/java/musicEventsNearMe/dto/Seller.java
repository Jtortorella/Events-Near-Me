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
@Table(name = "sellers")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private String identifier;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!(o instanceof Seller)) {
            return false;
        }
        Seller seller = (Seller) o;
        return Objects.equals(identifier, seller.identifier) &&
                Objects.equals(name, seller.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name);
    }
}
