package musicEventsNearMe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoCoordinatesResponseObject {
    private Long musicEventId;
    private double latitude;
    private double longitude;
}
