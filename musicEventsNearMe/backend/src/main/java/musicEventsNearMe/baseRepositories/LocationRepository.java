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

        @Query(value = "SELECT NAME, ' LOCATION' AS TYPE FROM LOCATIONS WHERE name LIKE %:name% LIMIT 2", nativeQuery = true)
        Optional<List<String>> searchByLocationName(@Param("name") String name);

        @Query(value = "SELECT Addresses.Street_Address, ' ADDRESS' AS TYPE "
                        +
                        "FROM Locations " +
                        "JOIN Addresses ON Locations.Address_Id = Addresses.Address_Id " +
                        "WHERE Addresses.Street_Address LIKE %:address% LIMIT 2", nativeQuery = true)
        Optional<List<String>> searchByAddress(@Param("address") String address);

}
