package musicEventsNearMe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import musicEventsNearMe.repositories.AddressCountryRepository;
import musicEventsNearMe.repositories.AddressRegionRepository;
import musicEventsNearMe.repositories.AddressRepository;
import musicEventsNearMe.repositories.GeoCoordinatesRepository;
import musicEventsNearMe.utilities.DataUtilities;
import musicEventsNearMe.baseRepositories.LocationRepository;
import musicEventsNearMe.dto.Address;
import musicEventsNearMe.dto.AddressCountry;
import musicEventsNearMe.dto.AddressRegion;
import musicEventsNearMe.dto.GeoCoordinate;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.entities.MusicEvent.Location;

@Service
@Transactional
@AllArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final DataUtilities dataUtilities;
    private final AddressRepository addressRepository;
    private final GeoCoordinatesRepository geoCoordinatesRepository;
    private final AddressRegionRepository addressRegionRepository;
    private final AddressCountryRepository addressCountryRepository;

    public LocationDTO saveEntityAndReturnEntity(LocationDTO location) {
        Address currentAddress = location.getAddress();
        if (currentAddress != null) {
            currentAddress.setAddressRegion(saveOrReturnPreviouslyAddressRegion(currentAddress.getAddressRegion()));
            currentAddress.setAddressCountry(saveOrReturnPreviouslyAddressCountry(currentAddress.getAddressCountry()));
            location.setAddress(saveOrReturnPreviouslySavedAddress(currentAddress));
        }
        if (location.getGeo() != null) {
            location.setGeo(saveOrReturnPreviouslySavedGeo(location.getGeo()));
        }
        return saveOrReturnPreviouslySavedLocation(location);
    }

    private Address saveOrReturnPreviouslySavedAddress(Address address) {
        return addressRepository
                .findByStreetAddressAndPostalCodeAndStreetAddress2(
                        address.getStreetAddress(),
                        address.getPostalCode(), address.getStreetAddress2())
                .orElseGet(() -> addressRepository.saveAndFlush(address));
    }

    private GeoCoordinate saveOrReturnPreviouslySavedGeo(GeoCoordinate geo) {
        return geoCoordinatesRepository.findByLatitudeAndLongitude(geo.getLatitude(),
                geo.getLongitude()).orElseGet(() -> geoCoordinatesRepository.saveAndFlush(geo));
    }

    private LocationDTO saveOrReturnPreviouslySavedLocation(LocationDTO locationDTO) {
        return locationRepository.findByIdentifier(locationDTO.getIdentifier())
                .orElseGet(() -> locationRepository.saveAndFlush(locationDTO));
    }

    private AddressRegion saveOrReturnPreviouslyAddressRegion(AddressRegion addressRegion) {
        return addressRegionRepository.findByNameAndIdentifier(addressRegion.getName(),
                addressRegion.getIdentifier()).orElseGet(() -> addressRegionRepository.saveAndFlush(addressRegion));
    }

    private AddressCountry saveOrReturnPreviouslyAddressCountry(AddressCountry addressCountry) {
        return addressCountryRepository.findByNameAndIdentifier(addressCountry.getName(),
                addressCountry.getIdentifier()).orElseGet(() -> addressCountryRepository.saveAndFlush(addressCountry));
    }

    public Optional<LocationDTO> getExistingLocation(Location location) {
        return locationRepository.findByIdentifier(location.getIdentifier());
    }

    public LocationDTO getLocationDTO(Location location) {
        return dataUtilities.getDTOEntityFromObject(location, LocationDTO.class);
    }

    public LocationDTO updateEntityAndReturnEntity(LocationDTO oldLocation, LocationDTO newLocation) {
        return oldLocation;
    }
}
