package musicEventsNearMe.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import musicEventsNearMe.entities.EventApiResponse;
import musicEventsNearMe.entities.MapBounds;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.repositories.MusicEventRepository;

@Service
public class ConcertDataService {

    private final LocalDateTime currentDateTime = LocalDateTime.now();

    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    @Autowired
    MusicEventRepository musicEventRepository;

    public ResponseEntity<List<MusicEvent>> getConcertDataForMap(MapBounds mapBounds) {
        mapBounds.checkForNegatives();
        return ResponseEntity.ok(musicEventRepository.findBetweenGeoCoordinates(mapBounds.getLatitudeLow(),
                mapBounds.getLatitudeHigh(),
                mapBounds.getLongitudeLow(), mapBounds.getLongitudeHigh()));
    }

    public void updateDatabaseWithConcertData() {
        int currentPage = 0;
        String url = buildApiUrl(currentPage);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);
        while (currentPage < response.getBody().getPagination().getTotalPages()) {
            currentPage++;
            url = buildApiUrl(currentPage);
            fetchEventsForPage(url);
        }
    }

    private String buildApiUrl(int currentPage) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("apikey", apiKey)
                .queryParam("page", currentPage)
                .toUriString();
    }

    private List<MusicEvent> fetchEventsForPage(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);
        for (MusicEvent event : response.getBody().getEvents()) {
            String identifier = event.getIdentifier();
            List<MusicEvent> found = musicEventRepository.findByIdentifier(identifier);
            if (checkForDuplicateData(found) == true) {
                checkForEntryLaterThanPreviouslyEnteredDateTime(event, found);
                checkForEntryModifcationLaterThanPreviouslyEnteredDateTime(event, found);
            } else {

                musicEventRepository.saveAndFlush(event);
            }

        }
        return response.getBody().getEvents();

    }

    private boolean checkForEntryModifcationLaterThanPreviouslyEnteredDateTime(MusicEvent event,
            List<MusicEvent> found) {
        for (MusicEvent foundEvent : found) {
            if (compareDatabaseRecordEnteredTimeAndEventsTime(event.getDateModified(),
                    foundEvent.getTimeRecordWasEntered())) {
                updateEntity(foundEvent, event);
                return true;
            }
        }
        return false;
    }

    private boolean checkForEntryLaterThanPreviouslyEnteredDateTime(MusicEvent event, List<MusicEvent> found) {
        for (MusicEvent foundEvent : found) {
            if (compareDatabaseRecordEnteredTimeAndEventsTime(event.getDatePublished(),
                    foundEvent.getTimeRecordWasEntered())) {
                updateEntity(foundEvent, event);
                return true;
            }
        }
        return false;
    }

    private boolean compareDatabaseRecordEnteredTimeAndEventsTime(String dt1, LocalDateTime dt2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTimeOfEventTime = LocalDateTime.parse(dt1, formatter);
        Duration duration = Duration.between(dateTimeOfEventTime, dt2);
        return duration.isNegative();
    }

    private void updateEntity(MusicEvent foundEvent, MusicEvent event) {
        // event.setId(foundEvent.getId());
        event.setTimeRecordWasEntered(currentDateTime);
        musicEventRepository.saveAndFlush(event);
    }

    private boolean checkForDuplicateData(List<MusicEvent> found) {
        if (found.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ResponseEntity<EventApiResponse> getEventsInMapArea(MapBounds mapBounds) {
        return null;
    }
}
