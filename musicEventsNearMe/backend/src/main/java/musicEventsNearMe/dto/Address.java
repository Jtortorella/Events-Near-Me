package musicEventsNearMe.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long address_id;
    private String addressType;
    private String streetAddress;
    private String postalCode;
    private String addressLocality;
    private String streetAddress2;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "address")
    private List<LocationDTO> locations;

    @ManyToOne
    @JoinColumn(name = "address_region_id")
    private AddressRegion addressRegion;

    @ManyToOne
    @JoinColumn(name = "address_country_id")
    private AddressCountry addressCountry;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return Objects.equals(addressType, address.addressType) &&
                Objects.equals(streetAddress, address.streetAddress) &&
                Objects.equals(addressLocality, address.addressLocality) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(addressRegion, address.addressRegion) &&
                Objects.equals(addressCountry, address.addressCountry) &&
                Objects.equals(streetAddress2, address.streetAddress2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressType, streetAddress, addressLocality, postalCode,
                addressRegion, addressCountry, streetAddress2);
    }
}