package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.entities.MusicEvent;
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

        @Query(value = "SELECT " +
                        "me.id AS music_event_id, me.custom_title, me.date_modified, me.date_published, me.door_time, me.end_date, "
                        +
                        "me.event_attendance_mode, me.event_status, me.event_type, me.headliner_in_support, me.identifier, me.image, "
                        +
                        "me.is_accessible_for_free, me.location_id, me.name, me.previous_start_date, me.promo_image, me.start_date, "
                        +
                        "me.subtitle, me.time_record_was_entered, me.url, " +
                        "l.id AS location_id, l.name AS location_name, l.identifier AS location_identifier, " +
                        "l.url AS location_url, l.image AS location_image, l.date_published AS location_date_published, "
                        +
                        "l.date_modified AS location_date_modified, l.maximum_attendee_capacity, " +
                        "a.id AS address_id, a.address_type, a.street_address, a.address_locality, a.postal_code, " +
                        "ar.id AS address_region_id, ar.address_region_type, " +
                        "ar.identifier AS address_region_identifier, ar.name AS address_region_name, " +
                        "ar.alternate_name AS address_region_alternate_name, ac.id AS address_country_id, ac.address_country_type, "
                        +
                        "p.id AS performer_id, p.name AS performer_name, p.identifier AS performer_identifier, " +
                        "p.url AS performer_url, p.image AS performer_image, p.date_published AS performer_date_published, "
                        +
                        "p.date_modified AS performer_date_modified, p.performer_type, p.foundingLocationId, p.foundingDate, "
                        +
                        "p.genre, p.band_or_musician, p.num_upcoming_events, p.performance_date, p.performance_rank, " +
                        "p.is_headliner, p.date_is_confirmed, " +
                        "ac.identifier AS address_country_identifier, ac.name AS address_country_name, " +
                        "ac.alternate_name AS address_country_alternate_name " +
                        "FROM music_events me " +
                        "JOIN locations l ON me.location_id = l.id " +
                        "JOIN addresses a ON l.address_id = a.id " +
                        "JOIN address_regions ar ON a.address_region_id = ar.id " +
                        "JOIN address_countries ac ON a.address_country_id = ac.id " +
                        "LEFT JOIN performances perf ON me.id = perf.music_event_id " +
                        "LEFT JOIN performers p ON perf.performer_id = p.id " +
                        "WHERE me.id = :eventId", nativeQuery = true)

        List<Object> findEventDetailsById(@Param("eventId") Long eventId);

}
