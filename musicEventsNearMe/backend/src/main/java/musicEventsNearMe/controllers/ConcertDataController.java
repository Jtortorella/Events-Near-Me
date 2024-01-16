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

import musicEventsNearMe.entities.GeoCoordinatesResponeObject;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.services.MusicEventService;

@RestController
@RequestMapping("/concertData")
@CrossOrigin(origins = "http://localhost:5173")
public class ConcertDataController {

    @Autowired
    private MusicEventService musicEventService;

    @GetMapping("/events")
    public ResponseEntity<List<GeoCoordinatesResponeObject>> getEvents(@ModelAttribute MapMaxims mapBounds) {
        return musicEventService.getConcertDataForMap(mapBounds);
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<Object> getEvents(@PathVariable Long id) {
        return musicEventService.getEventDetailsById(id);
    }

}
