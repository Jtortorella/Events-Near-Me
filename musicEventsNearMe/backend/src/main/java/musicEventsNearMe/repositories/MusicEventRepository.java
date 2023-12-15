package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import musicEventsNearMe.entities.MusicEvent;
import java.util.List;

@Repository
public interface MusicEventRepository extends JpaRepository<MusicEvent, Long> {
        List<MusicEvent> findByIdentifier(String identifier);

        @Query("SELECT me FROM MusicEvent me " +
                        "JOIN me.location loc " +
                        "JOIN loc.geo geo " +
                        "JOIN me.performer " +
                        "WHERE geo.latitude BETWEEN :latitudeLow AND :latitudeHigh " +
                        "AND geo.longitude BETWEEN :longitudeLow AND :longitudeHigh")
        List<MusicEvent> findBetweenGeoCoordinates(
                        @Param("latitudeLow") double latitudeLow,
                        @Param("latitudeHigh") double latitudeHigh,
                        @Param("longitudeLow") double longitudeLow,
                        @Param("longitudeHigh") double longitudeHigh);
}
