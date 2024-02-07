package musicEventsNearMe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.AddressCountry;

public interface AddressCountryRepository extends JpaRepository<AddressCountry, Long> {
    Optional<AddressCountry> findByNameAndIdentifier(String name, String identifier);
}
