package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.LocationDTO.Address;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreetAddressAndPostalCodeAndStreetAddress2(
            String streetAddress, String postalCode,
            String streetAddress2);
}