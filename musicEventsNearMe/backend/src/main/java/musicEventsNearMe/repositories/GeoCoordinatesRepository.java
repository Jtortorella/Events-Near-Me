package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.LocationDTO.GeoCoordinates;

import java.util.Optional;

public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinates, Long> {
    Optional<GeoCoordinates> findByLatitudeAndLongitude(double latitude, double longitude);
}