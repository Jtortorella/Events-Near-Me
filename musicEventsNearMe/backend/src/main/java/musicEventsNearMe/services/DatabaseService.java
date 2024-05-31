package musicEventsNearMe.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.AllArgsConstructor;
import musicEventsNearMe.entities.EventApiResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DatabaseService {

    private final EventService eventService;

    public void updateDatabaseWithConcertData() {
        try {
            ResponseEntity<EventApiResponse> response = new RestTemplate().getForEntity(buildApiUrl(0),
                    EventApiResponse.class);
            Optional.ofNullable(response.getBody())
                    .ifPresent(body -> {
                        body.getEvents().forEach((event) -> eventService.updateOrCreateMusicEvent(event));
                        for (int currentPage = 1; currentPage <= body.getPagination().getTotalPages(); currentPage++) {
                            try {
                                fetchEventsForPage(buildApiUrl(currentPage));
                            } catch (RestClientException e) {
                                System.err.println("Error message: " + e.getMessage());
                                System.err.println("Stack trace:");
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (RestClientException e) {
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();
        }
    }

    private void fetchEventsForPage(String url) throws RestClientException {
        ResponseEntity<EventApiResponse> response = new RestTemplate().getForEntity(url, EventApiResponse.class);
        Optional.ofNullable(response.getBody())
                .ifPresent(body -> body.getEvents().forEach((event) -> eventService.updateOrCreateMusicEvent(event)));
    }

    private String buildApiUrl(int currentPage) {
        String apiUrl = "https://www.jambase.com/jb-api/v1/events";
        return UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("page", currentPage)
                .queryParam("geoStateIso", "US-SC")
                .queryParam("eventDateFrom", getCurrentDate())
                .queryParam("eventDateTo", getMaximumDate())
                .queryParam("apikey", "be261bce-a04b-45fd-813b-bc31da5c73e7")
                .toUriString();
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(formatter);
    }

    private String getMaximumDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now().plusDays(30);
        return localDate.format(formatter);
    }
}
