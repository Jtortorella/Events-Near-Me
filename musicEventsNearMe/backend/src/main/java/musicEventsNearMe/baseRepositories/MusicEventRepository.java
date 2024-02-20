package musicEventsNearMe.baseRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.interfaces.BaseRepository;

@Repository
public interface MusicEventRepository extends BaseRepository<MusicEventDTO> {
        Optional<MusicEventDTO> findByIdentifier(String identifier);

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
                                        AND Geo_Coordinates.Longitude BETWEEN :longitudeLow AND :longitudeHigh
                        """, nativeQuery = true)
        List<Object[]> findBetweenGeoCoordinates(
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

}
