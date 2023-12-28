package musicEventsNearMe.repositories;

import java.util.Optional;

import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.interfaces.BaseRepository;

public interface PerformerRepository extends BaseRepository<PerformerDTO> {
    Optional<PerformerDTO> findByIdentifier(String identifier);
}
