package musicEventsNearMe.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.entities.MusicEvent.Location;
import musicEventsNearMe.entities.MusicEvent.Performer;

@Service
@AllArgsConstructor
public class EventService {

    private final LocationService locationService;
    private final MusicEventService musicEventService;
    private final PerformerService performerService;

    public MusicEventDTO updateOrCreateMusicEvent(MusicEvent APIResponseEvent) {
        Optional<MusicEventDTO> existingEvent = musicEventService.getExistingMusicEvent(APIResponseEvent);
        return existingEvent.isPresent() ? musicEventService.updateEntityAndReturnEntity(
                setLocationAndSetPerformers(existingEvent.get(), APIResponseEvent))
                : musicEventService.saveEntityAndReturnEntity(
                        setLocationAndSetPerformers(musicEventService.getMusicEventDTO(APIResponseEvent),
                                APIResponseEvent));
    }

    private MusicEventDTO setLocationAndSetPerformers(MusicEventDTO entityToBeSaved, MusicEvent APIResponseEvent) {
        entityToBeSaved.setLocation(updateLocationOrSaveLocation(APIResponseEvent.getLocation()));
        entityToBeSaved.setPerformer(updatePerformerOrSavePerformer(APIResponseEvent.getPerformer()));
        return entityToBeSaved;
    }

    private LocationDTO updateLocationOrSaveLocation(Location location) {
        Optional<LocationDTO> existingEntity = locationService.getExistingLocation(location);
        return existingEntity.isPresent()
                ? existingEntity.get()
                : locationService.saveEntityAndReturnEntity(locationService.getLocationDTO(location));
    }

    private List<PerformerDTO> updatePerformerOrSavePerformer(List<Performer> performers) {
        if (performers.size() > 0) {
            List<PerformerDTO> performerSet = new ArrayList<>();
            performers.forEach((importedPerformerFromAPI) -> {
                performerService.getExistingPerformer(importedPerformerFromAPI)
                        .ifPresentOrElse(
                                existingEntity -> {
                                    if (!performerSet.contains(existingEntity)) {
                                        performerSet.add(existingEntity);
                                    }
                                },
                                () -> {
                                    PerformerDTO newPerformer = performerService.saveEntityAndReturnEntity(
                                            performerService.getPerformerDTO(importedPerformerFromAPI));
                                    performerSet.add(newPerformer);
                                });
            });
            return performerSet;
        }
        return Collections.emptyList();
    }
}
