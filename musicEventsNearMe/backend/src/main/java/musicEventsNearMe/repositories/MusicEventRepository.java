package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.interfaces.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MusicEventRepository extends BaseRepository<MusicEventDTO> {
        Optional<MusicEventDTO> findByIdentifier(String identifier);

        @Query(value = "SELECT music_events.id, geo_coordinates.latitude, geo_coordinates.longitude " +
                        "FROM music_events " +
                        "JOIN locations ON music_events.location_id = locations.id " +
                        "JOIN geo_coordinates ON locations.geo_id = geo_coordinates.id " +
                        "WHERE (music_events.start_date BETWEEN :startDate AND :endDate OR music_events.end_date BETWEEN :startDate AND :endDate) "
                        +
                        "AND geo_coordinates.latitude BETWEEN :latitudeLow AND :latitudeHigh " +
                        "AND geo_coordinates.longitude BETWEEN :longitudeLow AND :longitudeHigh", nativeQuery = true)
        List<Object[]> findBetweenGeoCoordinates(
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}
