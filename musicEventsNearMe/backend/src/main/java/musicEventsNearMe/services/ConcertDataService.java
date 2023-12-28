package musicEventsNearMe.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformanceDTO;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.EventApiResponse;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.entities.MusicEvent.Location;
import musicEventsNearMe.entities.MusicEvent.Performer;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor // Generates a constructor with parameters for all fields
public class ConcertDataService {

    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    private final LocationService locationService;
    private final PerformanceService performanceService;
    private final MusicEventService musicEventService;
    private final PerformerService performerService;

    public void updateDatabaseWithConcertData() throws RestClientException {
        ResponseEntity<EventApiResponse> response = new RestTemplate().getForEntity(buildApiUrl(0),
                EventApiResponse.class);
        if (response.hasBody()) {
            for (int currentPage = 0; currentPage < response.getBody().getPagination().getTotalPages(); currentPage++) {
                fetchEventsForPage(buildApiUrl(currentPage));
            }
        }
    }

    private void fetchEventsForPage(String url) throws RestClientException {
        for (MusicEvent event : new RestTemplate().getForEntity(url, EventApiResponse.class).getBody().getEvents()) {
            processMusicEvent(event);
        }
    }

    private String buildApiUrl(int currentPage) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("apikey", apiKey)
                .queryParam("page", currentPage)
                .toUriString();
    }

    private void processMusicEvent(MusicEvent event) {
        if (musicEventService.checkIfEntityExists(event)) {
            updateExistingMusicEvent(event);
        } else {
            createNewMusicEvent(event);
        }
    }

    private void updateExistingMusicEvent(MusicEvent event) {
        MusicEventDTO existingMusicEventDTO = musicEventService.getExistingMusicEvent(event);
        MusicEventDTO newMusicEventDTO = musicEventService.getMusicEventDTO(event);

        if (!newMusicEventDTO.equals(existingMusicEventDTO)) {
            newMusicEventDTO.setLocationId(findAndUpdateLocation(event.getLocation()));
            if (existingMusicEventDTO == null) {
                updatePerformances(musicEventService.saveEntityAndReturnEntity(newMusicEventDTO).getId(),
                        findAndUpdatePerformers(event.getPerformer()));
            } else {
                if (musicEventService.checkIfEntityNeedsUpdating(newMusicEventDTO, existingMusicEventDTO)) {
                    musicEventService.updateEntity(newMusicEventDTO, existingMusicEventDTO);
                    updatePerformances(existingMusicEventDTO.getId(),
                            findAndUpdatePerformers(event.getPerformer()));
                } else {
                    return;
                }
            }
        }
    }

    private Long findAndUpdateLocation(Location location) {
        LocationDTO newLocationDTO = locationService.getLocationDTO(location);
        LocationDTO existingLocationDTO = locationService.getExistingLocation(location);

        if (existingLocationDTO == null) {
            return locationService.saveEntityAndReturnId(newLocationDTO);
        } else {
            if (newLocationDTO.equals(existingLocationDTO)) {
                return existingLocationDTO.getId();
            }
            if (locationService.checkIfEntityNeedsUpdating(newLocationDTO, existingLocationDTO)) {
                return locationService.updateEntityAndReturnId(newLocationDTO, existingLocationDTO);
            } else {
                return existingLocationDTO.getId();
            }
        }
    }

    private List<Long> findAndUpdatePerformers(List<Performer> performers) {
        List<Long> performersId = new ArrayList<>();

        for (Performer performer : performers) {
            PerformerDTO newPerformerDTO = performerService.getPerformerDTO(performer);
            PerformerDTO existingPerformerDTO = performerService.getExistingPerformer(performer);

            if (existingPerformerDTO == null) {
                performersId.add(performerService.saveEntityAndReturnId(newPerformerDTO));
            } else {
                if (newPerformerDTO.equals(existingPerformerDTO)) {
                    performersId.add(existingPerformerDTO.getId());
                } else {
                    if (performerService.checkIfEntityNeedsUpdating(newPerformerDTO, existingPerformerDTO)) {
                        performersId
                                .add(performerService.updateEntityAndReturnId(newPerformerDTO, existingPerformerDTO));
                    } else {
                        performersId.add(existingPerformerDTO.getId());
                    }
                }
            }
        }

        return performersId;
    }

    private void updatePerformances(Long currentMusicEventId, List<Long> currentPerformersIds) {
        List<PerformanceDTO> currentPerformances = createOrUpdatePerformances(currentMusicEventId,
                currentPerformersIds);
        if (!currentPerformances.isEmpty()) {
            deletePreviousPerformances(currentMusicEventId, currentPerformances);
        }
    }

    private List<PerformanceDTO> createOrUpdatePerformances(Long currentMusicEventId, List<Long> currentPerformersIds) {
        List<PerformanceDTO> currentPerformances = new ArrayList<>();
        for (Long performerId : currentPerformersIds) {
            if (!performanceService.checkIfEntityExists(currentMusicEventId, performerId)) {
                PerformanceDTO performance = createPerformance(currentMusicEventId, performerId);
                currentPerformances.add(performanceService.saveEntity(performance));
            } else {
                currentPerformances.add(performanceService.getExistingPerformance(currentMusicEventId, performerId));
            }
        }
        return currentPerformances;
    }

    private PerformanceDTO createPerformance(Long currentMusicEventId, Long performerId) {
        PerformanceDTO performance = new PerformanceDTO();
        performance.setMusicEvent(musicEventService.findById(currentMusicEventId));
        performance.setPerformer(performerService.findById(performerId));
        return performance;
    }

    private void deletePreviousPerformances(Long currentMusicEventId,
            List<PerformanceDTO> currentPerformances) {
        List<PerformanceDTO> previousPerformances = performanceService
                .getAllPerformancesForAMusicEvent(currentMusicEventId);
        for (PerformanceDTO previousPerformance : previousPerformances) {
            if (!currentPerformances.contains(previousPerformance))
                performanceService.deleteEntity(previousPerformance.getId());
        }
    }

    private void createNewMusicEvent(MusicEvent event) {
        MusicEventDTO newEntity = musicEventService.getMusicEventDTO(event);
        Long locationId = findAndUpdateLocation(event.getLocation());
        List<Long> performersId = findAndUpdatePerformers(event.getPerformer());
        newEntity.setLocationId(locationId);
        Long musicEventId = musicEventService.saveEntityAndReturnEntity(newEntity).getId();
        updatePerformances(musicEventId, performersId);
    }
}
