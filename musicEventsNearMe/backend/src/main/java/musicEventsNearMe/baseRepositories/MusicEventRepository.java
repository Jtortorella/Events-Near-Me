package musicEventsNearMe.baseRepositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.interfaces.BaseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

        @Query(value = "SELECT DISTINCT " +
                        "me.name, " +
                        "me.url, " +
                        "me.image, " +
                        "me.event_status, " +
                        "me.start_date, " +
                        "me.end_date, " +
                        "me.door_time, " +
                        "l.id AS location_id, " +
                        "l.name AS location_name, " +
                        "l.url AS location_url, " +
                        "a.street_address, " +
                        "a.address_locality, " +
                        "a.postal_code, " +
                        "me.is_accessible_for_free, " +
                        "me.promo_image, " +
                        "GROUP_CONCAT(DISTINCT CONCAT(" +
                        "p.id, ',', " +
                        "p.name, ',', p.url, ','," +
                        "p.num_upcoming_events" +
                        ") ORDER BY " +
                        "p.name SEPARATOR '|') AS performers_details " +
                        "FROM music_events me " +
                        "JOIN locations l ON me.location_id = l.id " +
                        "JOIN addresses a ON l.address_id = a.id " +
                        "JOIN address_regions ar ON a.address_region_id = ar.id " +
                        "JOIN address_countries ac ON a.address_country_id = ac.id " +
                        "LEFT JOIN performances perf ON me.id = perf.music_event_id " +
                        "LEFT JOIN performers p ON perf.performer_id = p.id " +
                        "WHERE me.id = :eventId", nativeQuery = true)
        Map<String, Object> findEventDetailsById(@Param("eventId") Long eventId);
}
