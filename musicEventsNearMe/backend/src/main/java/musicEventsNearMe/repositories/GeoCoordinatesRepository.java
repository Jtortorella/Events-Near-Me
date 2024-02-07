package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.GeoCoordinate;

import java.util.Optional;

public interface GeoCoordinatesRepository extends JpaRepository<GeoCoordinate, Long> {
    Optional<GeoCoordinate> findByLatitudeAndLongitude(double latitude, double longitude);
}