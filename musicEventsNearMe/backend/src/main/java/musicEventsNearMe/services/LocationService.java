package musicEventsNearMe.services;

import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.repositories.LocationRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Long saveLocationAndReturnId(LocationDTO location) {
        long index = checkForDuplicateData(location);
        if (index == -1l) {
            index = locationRepository.saveAndFlush(location).getId();
        }
        return index;
    }

    public Long checkForDuplicateData(LocationDTO location) {
        Optional<LocationDTO> found = locationRepository.findByIdentifier(location.getIdentifier());
        if (found.isPresent()) {
            return found.get().getId();
        }
        return -1l;
    }

}
