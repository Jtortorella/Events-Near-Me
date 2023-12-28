package musicEventsNearMe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.MusicEventDTO;
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

    public ResponseEntity<List<Object[]>> getConcertDataForMap(MapMaxims mapMaxim) {
        mapMaxim.checkForNegatives();
        return ResponseEntity.ok(musicEventRepository.findBetweenGeoCoordinates(
                mapMaxim.getLatitudeLow(), mapMaxim.getLatitudeHigh(),
                mapMaxim.getLongitudeLow(), mapMaxim.getLongitudeHigh()));
    }

    public boolean checkIfEntityExists(MusicEvent event) {
        return dataUtilities.checkForDuplicateDataAndReturnBoolean(
                dataUtilities.getDTOEntityFromObject(event, MusicEventDTO.class), musicEventRepository);
    }

    public MusicEventDTO findById(Long id) {
        return musicEventRepository.findById(id).orElse(null);
    }

    public boolean checkIfEntityNeedsUpdating(MusicEventDTO newEvent, MusicEventDTO originalEvent) {
        return dataUtilities.shouldUpdate(newEvent, originalEvent);
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
