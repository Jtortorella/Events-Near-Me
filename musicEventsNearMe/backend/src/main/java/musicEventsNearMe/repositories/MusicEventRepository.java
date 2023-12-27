package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.MusicEventDTO;
import java.util.List;
import java.util.Optional;

@Repository
public interface MusicEventRepository extends JpaRepository<MusicEventDTO, Long> {
        Optional<MusicEventDTO> findByIdentifier(String identifier);

        // @Query("SELECT me FROM MusicEventDTO me " +
        // "JOIN me.LocationDTO loc " +
        // "JOIN loc.geo geo " +
        // "JOIN me.performer " +
        // "WHERE geo.latitude BETWEEN :latitudeLow AND :latitudeHigh " +
        // "AND geo.longitude BETWEEN :longitudeLow AND :longitudeHigh")
        // List<MusicEventDTO> findBetweenGeoCoordinates(
        // @Param("latitudeLow") double latitudeLow,
        // @Param("latitudeHigh") double latitudeHigh,
        // @Param("longitudeLow") double longitudeLow,
        // @Param("longitudeHigh") double longitudeHigh);
}
