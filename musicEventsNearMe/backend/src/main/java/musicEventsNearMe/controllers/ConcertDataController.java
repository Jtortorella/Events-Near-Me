package musicEventsNearMe.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import musicEventsNearMe.entities.EventApiResponse;

@RestController
@RequestMapping("/concertData")
public class ConcertDataController {

    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    @GetMapping("/events") // Specify the endpoint path
    public ResponseEntity<EventApiResponse> getEvents() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("geoStateIso", "US-SC")
                    .queryParam("apikey", apiKey);
            String finalUrl = builder.toUriString();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(finalUrl, EventApiResponse.class);
            System.out.println(response);
            System.out.println("TESDF");
            return response;
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            e.printStackTrace(); // Replace with proper logging
        }
        return ResponseEntity.status(500).build(); // Return an appropriate response for error cases
    }
}
