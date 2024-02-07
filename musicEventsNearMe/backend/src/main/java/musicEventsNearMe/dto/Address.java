package musicEventsNearMe.dto;

import java.util.Objects;

import jakarta.persistence.CascadeType;
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
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long address_id;

    private String addressType;
    private String streetAddress;
    private String postalCode;

    private String addressLocality;

    @OneToOne
    @JoinColumn(name = "location_id")
    private LocationDTO location;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressRegion addressRegion;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressCountry addressCountry;

    private String streetAddress2;
    private String timezone;
    private int jamBaseMetroId;
    private int jamBaseCityId;

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
                Objects.equals(streetAddress2, address.streetAddress2) &&
                Objects.equals(timezone, address.timezone) &&
                jamBaseMetroId == address.jamBaseMetroId &&
                jamBaseCityId == address.jamBaseCityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressType, streetAddress, addressLocality, postalCode,
                addressRegion, addressCountry, streetAddress2, timezone, jamBaseMetroId, jamBaseCityId);
    }

}