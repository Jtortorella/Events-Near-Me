package musicEventsNearMe.baseRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.LocationDTO;
import musicEventsNearMe.interfaces.BaseRepository;

@Repository
public interface LocationRepository extends BaseRepository<LocationDTO> {
        Optional<LocationDTO> findByIdentifier(String identifier);

        @Query(value = "SELECT * FROM LOCATIONs WHERE name LIKE %:name%", nativeQuery = true)
        Optional<List<LocationDTO>> searchByLocationName(@Param("name") String name);

        @Query(value = "SELECT * FROM Locations JOIN Addresses ON Locations.address_id = Addresses.address_id WHERE Addresses.street_address LIKE %:address%", nativeQuery = true)
        Optional<List<Object[]>> searchByAddress(@Param("address") String address);
}
