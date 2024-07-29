package musicEventsNearMe.controllers;

import java.util.List;
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

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.entities.GeoCoordinatesResponseObject;
import musicEventsNearMe.entities.KeyWord;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.services.LocationService;
import musicEventsNearMe.services.MusicEventService;
import musicEventsNearMe.services.PerformerService;
import musicEventsNearMe.utilities.DataUtilities;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class MusicEventController {

    @Autowired
    private MusicEventService musicEventService;

    @Autowired
    private PerformerService performerService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/events/{id}")
    public ResponseEntity<MusicEventDTO> getEventDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok().body(musicEventService.getEventDetailsById(id).orElse(null));
    }

    /**
     * @param keyword is what the user entered into the search box on the front end,
     *                using the words it does a full text search for all the data in
     *                the database in performer, location, address and genre.
     * @return returns full text search results from the database.
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<KeyWord>> searchForKeyWordByUserInput(@PathVariable String keyword) {
        List<KeyWord> results = Stream.of(
                performerService.searchForPerformer(keyword),
                locationService.searchByLocationName(keyword),
                locationService.searchByAddress(keyword),
                performerService.searchForGenre(keyword))
                .flatMap(optional -> optional.orElseGet(List::of).stream())
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * @param mapBounds is what map maxims are (i.e. longitude low, longitude high,
     *                  latitude low, latitude high) from the front end.
     *                  The mapBounds correspond to what the map on the front end
     *                  displays to the user.
     * @returns all geo coordinates within those map bounds and the music event
     *          id(s) associated with them.
     *          This will allow users to click the marker associated with the geo
     *          coordinates and return the event details associated with the marker
     *          using the
     * @see getEventDetailsById() method in the same file.
     * 
     */
    @GetMapping("/events")
    public ResponseEntity<List<GeoCoordinatesResponseObject>> getGeoCoordinatesResponseFromMapBounds(
            @ModelAttribute MapMaxims mapBounds) {
        return ResponseEntity.ok()
                .body(musicEventService.getGeoCoordinatesResponseFromMapBounds(mapBounds));
    }

    @GetMapping("/events/search")
    public ResponseEntity<List<GeoCoordinatesResponseObject>> getGeoCoordinatesResponseFromKeyWord(
            @ModelAttribute KeyWord keyword) {
        return ResponseEntity.ok()
                .body(musicEventService.getGeoCoordinatesResponseFromKeyWord(keyword));
    }

    /**
     * @param keyword   Contains the search term and the type of search term in an
     *                  object.
     * @param mapBounds Contains the maps current latitude / longitude minimums and
     *                  maximums. Events are limited within the scope of the
     *                  minimums and maximums.
     * @return An empty list or an response entity of list of geo coordinates within
     *         the mapbounds and associated with the keyword
     */
    @GetMapping("/filtered-events/search")
    public ResponseEntity<List<GeoCoordinatesResponseObject>> getGeoCoordinatesResponseFromKeyWordAndMapBounds(
            @ModelAttribute KeyWord keyword,
            @ModelAttribute MapMaxims mapBounds) {
        return ResponseEntity.ok()
                .body(musicEventService.getGeoCoordinatesResponseFromKeyWordAndMapBounds(keyword, mapBounds));
    }
}
