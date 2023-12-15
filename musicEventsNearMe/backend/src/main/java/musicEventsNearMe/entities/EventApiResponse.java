package musicEventsNearMe.entities;

import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@ToString
public class EventApiResponse {
    private boolean success;
    private Pagination pagination;
    private List<MusicEvent> events;
    private Request request;

    @Data
    @ToString
    public static class Pagination {
        private int page;
        private int perPage;
        private int totalItems;
        private int totalPages;
        private String nextPage;
        private String previousPage;
    }

    @Data
    @ToString
    public static class Request {
        private String endpoint;
        private String method;
        private Object params;
        private String ip;
        private String userAgent;
    }

    @Data
    @ToString
    public static class UrlType {
        private String type;
        private String identifier;
        private String url;
    }
}
