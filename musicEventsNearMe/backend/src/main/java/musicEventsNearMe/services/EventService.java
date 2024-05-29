package musicEventsNearMe.services;

import java.util.List;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.entities.MusicEvent.Performer;

@Service
@AllArgsConstructor
public class EventService {

    private final LocationService locationService;
    private final MusicEventService musicEventService;
    private final PerformerService performerService;

    public void updateOrCreateMusicEvent(MusicEvent APIResponseEvent) {
        MusicEventDTO newEvent = musicEventService.getMusicEventDTO(APIResponseEvent);
        musicEventService.getExistingMusicEventByIdentifier(APIResponseEvent.getIdentifier())
                .ifPresentOrElse(existingEvent -> {
                    if (!existingEvent.equals(newEvent)) {
                        musicEventService.updateOrSaveEntityAndReturnEntity(
                                setLocationAndSetPerformers(newEvent, APIResponseEvent.getPerformer()));
                    }
                }, () -> {
                    musicEventService.saveEntityAndReturnEntity(
                            setLocationAndSetPerformers(newEvent, APIResponseEvent.getPerformer()));
                });
    }

    private MusicEventDTO setLocationAndSetPerformers(MusicEventDTO entityToBeSavedOrUpdated,
            List<Performer> performerList) {
        entityToBeSavedOrUpdated.setLocation(updateLocationOrSaveLocation(entityToBeSavedOrUpdated.getLocation()));
        entityToBeSavedOrUpdated.setPerformer(updatePerformerOrSavePerformer(performerList));
        return entityToBeSavedOrUpdated;
    }

    private LocationDTO updateLocationOrSaveLocation(LocationDTO locationDTO) {
        return locationDTO != null ? locationService.updateOrSaveEntityAndReturnEntity(locationDTO) : null;
    }

    private List<PerformerDTO> updatePerformerOrSavePerformer(List<Performer> performerList) {
        return performerService.updateOrSaveEntityAndReturnEntity(performerList);
    }
}
