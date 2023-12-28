package musicEventsNearMe.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapMaxims {
    private double latitudeHigh;
    private double latitudeLow;
    private double longitudeHigh;
    private double longitudeLow;
    private String startDate;
    private String endDate;

    public void checkForNegatives() {
        double temporaryVariable = 0.0;
        if (latitudeLow > latitudeHigh) {
            temporaryVariable = latitudeLow;
            latitudeLow = latitudeHigh;
            latitudeHigh = temporaryVariable;
        }
        if (longitudeLow > longitudeHigh) {
            temporaryVariable = longitudeLow;
            longitudeLow = longitudeHigh;
            longitudeHigh = temporaryVariable;
        }
    }
}
