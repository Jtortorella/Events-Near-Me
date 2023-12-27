package musicEventsNearMe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.LocationDTO;

public interface LocationRepository extends JpaRepository<LocationDTO, Long> {
        Optional<LocationDTO> findByIdentifier(String identifier);

}
