package musicEventsNearMe.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import musicEventsNearMe.dto.Genre;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.GeoCoordinatesResponeObject;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.services.LocationService;
import musicEventsNearMe.services.MusicEventService;
import musicEventsNearMe.services.PerformerService;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:5173")
public class ConcertDataController {

    @Autowired
    private MusicEventService musicEventService;

    @Autowired
    private PerformerService performerService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/events")
    public ResponseEntity<List<GeoCoordinatesResponeObject>> getEvents(@ModelAttribute MapMaxims mapBounds) {
        return musicEventService.getConcertDataForMap(mapBounds);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<MusicEventDTO> getEvents(@PathVariable Long id) {
        return musicEventService.getEventDetailsById(id);
    }

    @GetMapping("/performer/{name}")
    public ResponseEntity<List<PerformerDTO>> searchForPerformers(@PathVariable String name) {
        return ResponseEntity.ok().body(performerService.searchForPerformer(name).orElse(null));
    }

    @GetMapping("/location/{name}")
    public ResponseEntity<List<LocationDTO>> searchForLocationByName(@PathVariable String name) {
        return ResponseEntity.ok().body(locationService.searchByLocationName(name).orElse(null));
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<List<Object[]>> searchForLocationByAddress(@PathVariable String address) {
        return ResponseEntity.ok().body(locationService.searchByAddress(address).orElse(null));
    }

    @GetMapping("/genre/{name}")
    public ResponseEntity<List<Genre>> searchForGenreByName(@PathVariable String name) {
        return ResponseEntity.ok().body(performerService.searchForGenre(name).orElse(null));
    }
}
