package musicEventsNearMe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.entities.MusicEvent.Location;
import musicEventsNearMe.repositories.LocationRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    DataUtilities dataUtilities;

    public boolean checkIfEntityExists(Location location) {
        return dataUtilities.checkForDuplicateDataAndReturnBoolean(
                dataUtilities.getDTOEntityFromObject(location, LocationDTO.class), locationRepository);
    }

    public boolean checkIfEntityNeedsUpdating(LocationDTO newLocation, LocationDTO oldLocation) {
        return dataUtilities.shouldUpdate(newLocation, oldLocation);
    }

    public Long saveEntityAndReturnId(LocationDTO location) {
        return locationRepository.saveAndFlush(location).getId();
    }

    public LocationDTO getExistingLocation(Location location) {
        return locationRepository.findByIdentifier(location.getIdentifier()).orElse(null);
    }

    public LocationDTO getLocationDTO(Location location) {
        return dataUtilities.getDTOEntityFromObject(location, LocationDTO.class);
    }

    public Long updateEntityAndReturnId(LocationDTO newLocation, LocationDTO oldLocation) {
        return dataUtilities.updateEntity(newLocation, oldLocation, locationRepository).getId();
    }
}
