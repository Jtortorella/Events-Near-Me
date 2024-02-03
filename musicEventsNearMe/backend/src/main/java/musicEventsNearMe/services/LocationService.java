package musicEventsNearMe.services;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.Data;
import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.dto.LocationDTO.Address;
import musicEventsNearMe.dto.LocationDTO.AddressCountry;
import musicEventsNearMe.dto.LocationDTO.AddressRegion;
import musicEventsNearMe.dto.LocationDTO.GeoCoordinates;
import musicEventsNearMe.repositories.AddressCountryRepository;
import musicEventsNearMe.repositories.AddressRegionRepository;
import musicEventsNearMe.repositories.AddressRepository;
import musicEventsNearMe.repositories.GeoCoordinatesRepository;
import musicEventsNearMe.utilities.DataUtilities;
import musicEventsNearMe.baseRepositories.LocationRepository;
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

        // Check if addressRegion and addressCountry have been previously saved
        currentAddress.setAddressRegion(saveOrUpdateAddressRegion(currentAddress.getAddressRegion()));
        currentAddress.setAddressCountry(saveOrUpdateAddressCountry(currentAddress.getAddressCountry()));

        // Check if geo has been previously saved
        location.setGeo(saveOrUpdateGeo(location.getGeo()));

        // Check if address has been previously saved
        currentAddress = saveOrUpdateAddress(currentAddress);
        location.setAddress(currentAddress);

        // Check if location has been previously saved
        return saveOrUpdateLocation(location).getId();
    }

    private Address saveOrUpdateAddress(Address address) {
        return addressRepository
                .findByStreetAddressAndPostalCodeAndStreetAddress2(
                        address.getStreetAddress(),
                        address.getPostalCode(), address.getStreetAddress2())
                .orElseGet(() -> addressRepository.saveAndFlush(address));
    }

    private GeoCoordinates saveOrUpdateGeo(GeoCoordinates geo) {
        return geoCoordinatesRepository.findByLatitudeAndLongitude(geo.getLatitude(),
                geo.getLongitude()).orElseGet(() -> geoCoordinatesRepository.saveAndFlush(geo));
    }

    private LocationDTO saveOrUpdateLocation(LocationDTO locationDTO) {
        return locationRepository.findByIdentifier(locationDTO.getIdentifier())
                .orElseGet(() -> locationRepository.saveAndFlush(locationDTO));
    }

    private AddressRegion saveOrUpdateAddressRegion(AddressRegion addressRegion) {
        return addressRegionRepository.findByNameAndIdentifier(addressRegion.getName(),
                addressRegion.getIdentifier()).orElseGet(() -> addressRegionRepository.saveAndFlush(addressRegion));
    }

    private AddressCountry saveOrUpdateAddressCountry(AddressCountry addressCountry) {
        return addressCountryRepository.findByNameAndIdentifier(addressCountry.getName(),
                addressCountry.getIdentifier()).orElseGet(() -> addressCountryRepository.saveAndFlush(addressCountry));
    }

    public LocationDTO getExistingLocation(Location location) {
        return locationRepository.findByIdentifier(location.getIdentifier()).orElse(null);
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
