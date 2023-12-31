package musicEventsNearMe.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.entities.GeoCoordinatesResponeObject;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.repositories.MusicEventRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class MusicEventService {

    @Autowired
    MusicEventRepository musicEventRepository;

    @Autowired
    DataUtilities dataUtilities;

    public ResponseEntity<List<GeoCoordinatesResponeObject>> getConcertDataForMap(MapMaxims mapMaxim) {
        mapMaxim.checkForNegatives();
        return ResponseEntity.ok().body(
                musicEventRepository.findBetweenGeoCoordinates(
                        mapMaxim.getLatitudeLow(), mapMaxim.getLatitudeHigh(),
                        mapMaxim.getLongitudeLow(), mapMaxim.getLongitudeHigh(),
                        convertToLocalDateTime(mapMaxim.getStartDate()), convertToLocalDateTime(mapMaxim.getEndDate()))
                        .stream()
                        .map(response -> new GeoCoordinatesResponeObject((Long) response[0], (double) response[1],
                                (double) response[2]))
                        .collect(Collectors.toList()));

    }

    public LocalDateTime convertToLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }

    public MusicEventDTO findById(Long id) {
        return musicEventRepository.findById(id).orElse(null);
    }

    public MusicEventDTO saveEntityAndReturnEntity(MusicEventDTO event) {
        return musicEventRepository.saveAndFlush(event);
    }

    public MusicEventDTO getExistingMusicEvent(MusicEvent event) {
        return musicEventRepository.findByIdentifier(event.getIdentifier()).orElse(null);
    }

    public MusicEventDTO getMusicEventDTO(MusicEvent event) {
        return dataUtilities.getDTOEntityFromObject(event, MusicEventDTO.class);
    }

    public void updateEntity(MusicEventDTO newEvent, MusicEventDTO oldEvent) {
        dataUtilities.updateEntity(newEvent, oldEvent, musicEventRepository);
    }

}
