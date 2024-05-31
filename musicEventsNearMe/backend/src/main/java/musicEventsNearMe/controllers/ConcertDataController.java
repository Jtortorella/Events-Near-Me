package musicEventsNearMe.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/search/{keyword}")
    public List<String> searchByKeyWord(@PathVariable String keyword) {
        return Stream.of(
                performerService.searchForPerformer(keyword),
                locationService.searchByLocationName(keyword),
                locationService.searchByAddress(keyword),
                performerService.searchForGenre(keyword))
                .flatMap(optional -> optional.orElseGet(List::of).stream())
                .collect(Collectors.toList());
    }
}
