package musicEventsNearMe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeoCoordinatesResponeObject {
    private Long musicEventId;
    private double latitude;
    private double longitude;
}
