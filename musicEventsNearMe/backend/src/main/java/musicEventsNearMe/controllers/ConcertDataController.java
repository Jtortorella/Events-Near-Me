package musicEventsNearMe.controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import musicEventsNearMe.entities.EventApiResponse;
import musicEventsNearMe.services.ConcertDataService;

@RestController
@RequestMapping("/concertData")
public class ConcertDataController {

    @Autowired
    private ConcertDataService concertDataService;

    @GetMapping("/events") // Specify the endpoint path
    public ResponseEntity<EventApiResponse> getEvents() {
        return concertDataService.getAllConcertData();
    }
}
