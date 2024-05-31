package musicEventsNearMe.baseRepositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.interfaces.BaseRepository;

@Repository
public interface PerformerRepository extends BaseRepository<PerformerDTO> {
    Optional<PerformerDTO> findByIdentifier(String identifier);

    @Query(value = "SELECT NAME, ' PERFORMER NAME' AS TYPE FROM PERFORMERS WHERE name LIKE %:name% LIMIT 2", nativeQuery = true)
    Optional<List<String>> searchByPerformerName(@Param("name") String name);

}
