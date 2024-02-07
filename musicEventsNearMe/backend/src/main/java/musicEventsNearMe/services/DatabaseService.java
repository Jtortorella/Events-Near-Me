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
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.entities.MusicEvent.Location;
import musicEventsNearMe.entities.MusicEvent.Performer;
import musicEventsNearMe.dto.LocationDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DatabaseService {

    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    private final LocationService locationService;
    private final PerformanceService performanceService;
    private final MusicEventService musicEventService;
    private final PerformerService performerService;

    public void updateDatabaseWithConcertData() {
        try {
            ResponseEntity<EventApiResponse> response = new RestTemplate().getForEntity(buildApiUrl(0),
                    EventApiResponse.class);
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> {
                        body.getEvents().forEach(this::updateOrCreateMusicEvent);
                        for (int currentPage = 1; currentPage < body.getPagination().getTotalPages(); currentPage++) {
                            try {
                                fetchEventsForPage(buildApiUrl(currentPage));
                            } catch (RestClientException e) {
                            }
                        }
                    });
        } catch (RestClientException e) {
        }
    }

    private void fetchEventsForPage(String url) throws RestClientException {
        ResponseEntity<EventApiResponse> response = new RestTemplate().getForEntity(url, EventApiResponse.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(body -> body.getEvents().forEach(this::updateOrCreateMusicEvent));
    }

    private void updateOrCreateMusicEvent(MusicEvent event) {
        MusicEventDTO existingMusicEventDTO = musicEventService.getExistingMusicEvent(event);
        MusicEventDTO newMusicEventDTO = musicEventService.getMusicEventDTO(event);
        newMusicEventDTO.setLocationId(findAndUpdateLocation(event.getLocation()));
        if (!newMusicEventDTO.equals(existingMusicEventDTO)) {
            updateMusicEventAndPerformances(newMusicEventDTO, existingMusicEventDTO, event.getPerformer());
        }
    }

    private Long findAndUpdateLocation(Location location) {
        LocationDTO newLocationDTO = locationService.getLocationDTO(location);
        LocationDTO existingLocationDTO = locationService.getExistingLocation(location).orElse(null);
        return existingLocationDTO == null
                ? locationService.saveEntityAndReturnId(newLocationDTO)
                : locationService.updateEntityAndReturnId(newLocationDTO, existingLocationDTO);
    }

    private void updateMusicEventAndPerformances(MusicEventDTO newMusicEventDTO, MusicEventDTO existingMusicEventDTO,
            List<Performer> performers) {
        Long musicEventId = existingMusicEventDTO == null
                ? musicEventService.saveEntityAndReturnEntity(newMusicEventDTO).getId()
                : existingMusicEventDTO.getId();
        updatePerformances(musicEventId, findAndUpdatePerformers(performers));
    }

    private List<Long> findAndUpdatePerformers(List<Performer> performers) {
        return performers.stream()
                .map(performer -> {
                    PerformerDTO newPerformerDTO = performerService.getPerformerDTO(performer);
                    PerformerDTO existingPerformerDTO = performerService.getExistingPerformer(performer);
                    return existingPerformerDTO == null
                            ? performerService.saveEntityAndReturnId(newPerformerDTO)
                            : performerService.updateEntityAndReturnId(newPerformerDTO, existingPerformerDTO);
                })
                .collect(Collectors.toList());
    }

    private void updatePerformances(Long currentMusicEventId, List<Long> currentPerformersIds) {
        List<PerformanceDTO> currentPerformances = createOrUpdatePerformances(currentMusicEventId,
                currentPerformersIds);
        if (!currentPerformances.isEmpty()) {
            deletePreviousPerformances(currentMusicEventId, currentPerformances);
        }
    }

    private List<PerformanceDTO> createOrUpdatePerformances(Long currentMusicEventId, List<Long> currentPerformersIds) {
        return currentPerformersIds.stream()
                .map(performerId -> performanceService.checkIfEntityExists(currentMusicEventId, performerId)
                        ? performanceService.getExistingPerformance(currentMusicEventId, performerId)
                        : performanceService.saveEntity(createPerformance(currentMusicEventId, performerId)))
                .collect(Collectors.toList());
    }

    private PerformanceDTO createPerformance(Long currentMusicEventId, Long performerId) {
        PerformanceDTO performance = new PerformanceDTO();
        performance.setMusicEvent(musicEventService.findById(currentMusicEventId));
        performance.setPerformer(performerService.findById(performerId));
        return performanceService.saveEntity(performance);
    }

    private void deletePreviousPerformances(Long currentMusicEventId, List<PerformanceDTO> currentPerformances) {
        List<PerformanceDTO> previousPerformances = performanceService
                .getAllPerformancesForAMusicEvent(currentMusicEventId);
        previousPerformances.stream()
                .filter(previousPerformance -> !currentPerformances.contains(previousPerformance))
                .forEach(previousPerformance -> performanceService.deleteEntity(previousPerformance.getId()));
    }

    private String buildApiUrl(int currentPage) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("apikey", apiKey)
                .queryParam("page", currentPage)
                .toUriString();
    }
}
