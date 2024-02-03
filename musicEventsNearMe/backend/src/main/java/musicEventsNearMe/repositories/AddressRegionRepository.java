package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.LocationDTO.AddressRegion;

import java.util.Optional;

public interface AddressRegionRepository extends JpaRepository<AddressRegion, Long> {
    Optional<AddressRegion> findByNameAndIdentifier(String name, String identifier);
}
