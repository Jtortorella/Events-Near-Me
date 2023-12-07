package musicEventsNearMe.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import musicEventsNearMe.entities.EventApiResponse;

@Service
public class ConcertDataService {

    private final String apiKey = "be261bce-a04b-45fd-813b-bc31da5c73e7";
    private final String apiUrl = "https://www.jambase.com/jb-api/v1/events";

    public ResponseEntity<EventApiResponse> getAllConcertData() {
        int currentPage = 1;
        String url = buildApiUrl(currentPage);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);

        while (currentPage < response.getBody().getPagination().getTotalPages()) {
            currentPage++;
            url = buildApiUrl(currentPage);
            response.getBody().getEvents().addAll(fetchEventsForPage(url));
        }
        return response;
    }

    private String buildApiUrl(int currentPage) {
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("apikey", apiKey)
                .queryParam("page", currentPage)
                .toUriString();
    }

    private List<EventApiResponse.Event> fetchEventsForPage(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EventApiResponse> response = restTemplate.getForEntity(url, EventApiResponse.class);
        return response.getBody().getEvents();
    }
}
