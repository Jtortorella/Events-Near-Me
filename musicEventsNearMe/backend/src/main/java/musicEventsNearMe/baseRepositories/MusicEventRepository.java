package musicEventsNearMe.baseRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.interfaces.BaseRepository;

@Repository
public interface MusicEventRepository extends BaseRepository<MusicEventDTO> {

        Optional<MusicEventDTO> findByIdentifier(String identifier);

        @Modifying
        @Transactional
        @Query(value = """
                        DELETE
                        FROM Music_Events
                        WHERE Music_Events.Start_Date < NOW() - INTERVAL 1 DAY
                        """, nativeQuery = true)
        int deletePreviousEvents();

        @Query(value = """
                            SELECT
                                Music_Events.Music_Event_ID,
                                Geo_Coordinates.Latitude,
                                Geo_Coordinates.Longitude
                            FROM
                                Music_Events
                            JOIN
                                Locations ON Music_Events.Location_ID = Locations.Location_ID
                            JOIN
                                Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                            WHERE
                                (Music_Events.Start_Date BETWEEN :startDate AND :endDate
                                OR Music_Events.End_Date BETWEEN :startDate AND :endDate)
                                AND Geo_Coordinates.Latitude BETWEEN :latitudeLow AND :latitudeHigh
                                AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh;
                        """, nativeQuery = true)
        List<Object[]> findBetweenGeoCoordinates(
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Performers
                                JOIN Music_Events_Performer ON Performers.ID = Music_Events_Performer.PERFORMER_ID
                                JOIN Music_Events ON Music_Events_Performer.Music_EventDTO_Music_Event_ID = Music_Events.Music_Event_ID
                                JOIN Locations ON Music_Events.Location_ID = Locations.Location_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                WHERE Performers.NAME = :performerName;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventByPerformerNameForAllEvents(
                        @Param("performerName") String performerName);

        @Query(value = """
                            SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                            FROM Performers
                            JOIN Music_Events_Performer ON Performers.ID = Music_Events_Performer.PERFORMER_ID
                            JOIN Music_Events ON Music_Events_Performer.Music_EventDTO_Music_Event_ID = Music_Events.Music_Event_ID
                            JOIN Locations ON Music_Events.Location_ID = Locations.Location_ID
                            JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                            WHERE
                                (Music_Events.Start_Date BETWEEN :startDate AND :endDate
                                OR Music_Events.End_Date BETWEEN :startDate AND :endDate)
                                AND Geo_Coordinates.Latitude BETWEEN :latitudeLow AND :latitudeHigh
                                AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh
                                AND Performers.NAME = :performerName;
                        """, nativeQuery = true)
        List<Object[]> findMusicEventByPerformerNameBetweenGeoAndDates(
                        @Param("performerName") String performerName,
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Performers
                                JOIN Music_Events_Performer ON Performers.ID = Music_Events_Performer.PERFORMER_ID
                                JOIN Music_Events ON Music_Events_Performer.Music_EventDTO_Music_Event_ID = Music_Events.Music_Event_ID
                                JOIN Locations ON Music_Events.Location_ID = Locations.Location_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                JOIN Performers_Genres ON Performers.ID = Performers_Genres.PerformerDTO_ID
                                JOIN Genres ON Genres.Genre_ID = Performers_Genres.Genres_Genre_ID
                                WHERE Genres.Genre_Name = :genreName;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventsByGenreNameForAllEvents(
                        @Param("genreName") String genreName);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Performers
                                    JOIN Music_Events_Performer ON Performers.ID = Music_Events_Performer.PERFORMER_ID
                                    JOIN Music_Events ON Music_Events_Performer.Music_EventDTO_Music_Event_ID = Music_Events.Music_Event_ID
                                    JOIN Performers_Genres ON Performers.ID = Performers_Genres.PerformerDTO_ID
                                    JOIN Genres ON Genres.Genre_ID = Performers_Genres.Genres_Genre_ID
                                    JOIN Locations ON Music_Events.Location_ID = Locations.Location_ID
                                    JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                    WHERE
                                        (Music_Events.Start_Date BETWEEN :startDate AND :endDate
                                        OR Music_Events.End_Date BETWEEN :startDate AND :endDate)
                                        AND Geo_Coordinates.Latitude BETWEEN :latitudeLow AND :latitudeHigh
                                        AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh
                                        AND Genres.Genre_Name = :genreName;
                                """, nativeQuery = true)
        List<Object[]> findMusicEventByGenreBetweenGeoAndDates(
                        @Param("genreName") String genreName,
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Addresses
                                JOIN Locations ON Addresses.Address_ID = Locations.Address_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                JOIN Music_Events ON Music_Events.Location_ID = Locations.Location_ID
                                WHERE Addresses.Street_Address = :streetAddress;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventsByStreetAddressForAllEvents(
                        @Param("streetAddress") String streetAddress);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Addresses
                                JOIN Locations ON Addresses.Address_ID = Locations.Address_ID
                                JOIN Music_Events ON Music_Events.Location_ID = Locations.Location_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                WHERE
                                    (Music_Events.Start_Date BETWEEN :startDate AND :endDate
                                    OR Music_Events.End_Date BETWEEN :startDate AND :endDate)
                                    AND Geo_Coordinates.Latitude BETWEEN :latitudeLow AND :latitudeHigh
                                    AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh
                                    AND Addresses.Street_Address = :streetAddress;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventByStreetAddressBetweenGeoAndDates(
                        @Param("streetAddress") String streetAddress,
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Locations
                                JOIN Music_Events ON Music_Events.Location_ID = Locations.Location_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                WHERE Locations.Name = :locationName;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventsByLocationNameForAllEvents(
                        @Param("locationName") String locationName);

        @Query(value = """
                        SELECT Music_Events.Music_Event_ID, Geo_Coordinates.Latitude, Geo_Coordinates.Longitude
                        FROM Music_Events
                                JOIN Locations ON Music_Events.Location_ID = Locations.Location_ID
                                JOIN Geo_Coordinates ON Locations.Geo_ID = Geo_Coordinates.Geo_ID
                                WHERE
                                    (Music_Events.Start_Date BETWEEN :startDate AND :endDate
                                    OR Music_Events.End_Date BETWEEN :startDate AND :endDate)
                                    AND Geo_Coordinates.Latitude BETWEEN :latitudeLow AND :latitudeHigh
                                    AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh
                                    AND Locations.Name = :name;
                            """, nativeQuery = true)
        List<Object[]> findMusicEventByLocationBetweenGeoAndDates(
                        @Param("name") String name,
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}
