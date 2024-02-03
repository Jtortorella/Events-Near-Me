package musicEventsNearMe.baseRepositories;

import java.util.Optional;

import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.interfaces.BaseRepository;

public interface LocationRepository extends BaseRepository<LocationDTO> {
        Optional<LocationDTO> findByIdentifier(String identifier);
}
