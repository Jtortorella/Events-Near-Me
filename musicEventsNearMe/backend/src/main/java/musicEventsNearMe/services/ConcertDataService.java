package musicEventsNearMe.services;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import musicEventsNearMe.Interfaces.BaseEntity;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.EventApiResponse;
import musicEventsNearMe.entities.MapBounds;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.entities.MusicEvent.Performer;
import musicEventsNearMe.repositories.MusicEventRepository;

@Service
public class ConcertDataService {
    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    @Autowired
    MusicEventRepository musicEventRepository;

    @Autowired
    LocationService locationService;

    @Autowired
    PerformerService performerService;

    public ResponseEntity<List<MusicEventDTO>> getConcertDataForMap(MapBounds mapBounds) {
        mapBounds.checkForNegatives();
        // return
        // ResponseEntity.ok(musicEventRepository.findBetweenGeoCoordinates(mapBounds.getLatitudeLow(),
        // mapBounds.getLatitudeHigh(),
        // mapBounds.getLongitudeLow(), mapBounds.getLongitudeHigh()));
        return null;
    }

    public void updateDatabaseWithConcertData() {
        int currentPage = 0;
        String url = buildApiUrl(currentPage);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);
        if (response.hasBody()) {
            while (currentPage < response.getBody().getPagination().getTotalPages()) {
                currentPage++;
                url = buildApiUrl(currentPage);
                fetchEventsForPage(url);
            }
        }

    }

    private String buildApiUrl(int currentPage) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("apikey", apiKey)
                .queryParam("page", currentPage)
                .toUriString();
    }

    private void fetchEventsForPage(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);
        ModelMapper modelMapper = new ModelMapper();
        if (response.hasBody()) {
            for (MusicEvent event : response.getBody().getEvents()) {
                String identifier = event.getIdentifier();
                Optional<MusicEventDTO> found = musicEventRepository.findByIdentifier(identifier);
                MusicEventDTO entity = modelMapper.map(event, MusicEventDTO.class);
                if (checkForDuplicateData(found) == true) {
                    checkForEntryLaterThanPreviouslyEnteredDateTime(entity, found, musicEventRepository);
                } else {
                    entity.setLocationId(locationService
                            .saveLocationAndReturnId(
                                    modelMapper.map(event.getLocation(), LocationDTO.class)));
                    System.out.println(entity.getLocationId());

                    entity.setPerformersId(
                            performerService.savePerformersAndReturnIds(
                                    modelMapper.map(event.getPerformer(),
                                            new TypeToken<List<PerformerDTO>>() {
                                            }.getType())));
                    System.out.println(entity.getPerformersId());

                    musicEventRepository.saveAndFlush(entity);
                }
            }
        }
    }

    public <D, T> D getDTOEntityFromObject(T data, Type type) {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(data, type);
    }

    public <D, T extends BaseEntity> boolean checkForEntryLaterThanPreviouslyEnteredDateTime(T data,
            Optional<T> found,
            JpaRepository<T, Long> repository) {
        if (found.isPresent() && (compareDatabaseRecordEnteredTimeAndEventsTime(found.get().getDateModified(),
                data.getTimeRecordWasEntered())
                || compareDatabaseRecordEnteredTimeAndEventsTime(
                        found.get().getDatePublished(), data.getTimeRecordWasEntered()))) {
            updateEntity(found.get(), data, repository);
            return true;
        }
        return false;
    }

    private <T> boolean compareDatabaseRecordEnteredTimeAndEventsTime(String dt1, LocalDateTime dt2) {
        if (dt1 == null) {
            // Handle the case where dt1 is null (you might choose to return false or throw
            // an exception)
            return false;
        }
        if (dt2 == null) {
            return false;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
        LocalDateTime dateTimeOfEventTime = LocalDateTime.parse(dt1, formatter);
        Duration duration = Duration.between(dateTimeOfEventTime, dt2);
        return duration.isNegative();
    }

    public <T extends BaseEntity> void updateEntity(T foundEvent, T event, JpaRepository<T, Long> repository) {
        if (foundEvent != null) {
            // Assuming there is an 'id' field in your entities
            ModelMapper modelMapper = new ModelMapper();

            modelMapper.map(event, foundEvent);
            event.setTimeRecordWasEntered(LocalDateTime.now());
            repository.saveAndFlush(foundEvent);
        }
    }

    public <T> boolean checkForDuplicateData(Optional<T> found) {
        return found.isPresent();
    }

    public <D, T> List<T> getDTOEntityFromObjectList(List<Performer> list, Type type) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(list, typeToken.getType());
    }

}
