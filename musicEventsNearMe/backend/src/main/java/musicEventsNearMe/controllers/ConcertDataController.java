package musicEventsNearMe.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import musicEventsNearMe.entities.MapBounds;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.services.ConcertDataService;

@RestController
@RequestMapping("/concertData")
@CrossOrigin(origins = "http://localhost:5173")
public class ConcertDataController {

    @Autowired
    private ConcertDataService concertDataService;

    @GetMapping("/events")
    public ResponseEntity<List<MusicEvent>> getEvents(@ModelAttribute MapBounds mapBounds) {
        return concertDataService.getConcertDataForMap(mapBounds);
    }

}
