package musicEventsNearMe.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "address_regions")
public class AddressRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long address_region_id;
    @JsonIgnore
    private String identifier;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "addressRegion")
    private List<Address> addresses;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AddressRegion that = (AddressRegion) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name);
    }
}
