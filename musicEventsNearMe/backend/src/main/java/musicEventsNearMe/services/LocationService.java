package musicEventsNearMe.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Data;
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
@Data
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;
    private final DataUtilities dataUtilities;
    private final AddressRepository addressRepository;
    private final GeoCoordinatesRepository geoCoordinatesRepository;
    private final AddressRegionRepository addressRegionRepository;
    private final AddressCountryRepository addressCountryRepository;

    public LocationService(
            LocationRepository locationRepository,
            DataUtilities dataUtilities,
            AddressRepository addressRepository,
            GeoCoordinatesRepository geoCoordinatesRepository,
            AddressRegionRepository addressRegionRepository,
            AddressCountryRepository addressCountryRepository) {
        this.locationRepository = locationRepository;
        this.dataUtilities = dataUtilities;
        this.addressRepository = addressRepository;
        this.geoCoordinatesRepository = geoCoordinatesRepository;
        this.addressRegionRepository = addressRegionRepository;
        this.addressCountryRepository = addressCountryRepository;
    }

    public Long saveEntityAndReturnId(LocationDTO location) {
        Address currentAddress = location.getAddress();
        currentAddress.setAddressRegion(saveOrReturnPreviouslyAddressRegion(currentAddress.getAddressRegion()));
        currentAddress.setAddressCountry(saveOrReturnPreviouslyAddressCountry(currentAddress.getAddressCountry()));
        location.setGeo(saveOrReturnPreviouslySavedGeo(location.getGeo()));
        location.setAddress(saveOrReturnPreviouslySavedAddress(currentAddress));
        return saveOrReturnPreviouslySavedLocation(location).getId();
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

    public Long updateEntityAndReturnId(LocationDTO newLocation, LocationDTO oldLocation) {
        return 1l;
        // return dataUtilities.updateEntity(newLocation, oldLocation,
        // locationRepository).getId();
    }
}
