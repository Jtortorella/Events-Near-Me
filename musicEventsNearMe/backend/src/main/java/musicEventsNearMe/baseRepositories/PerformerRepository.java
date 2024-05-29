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

    @Query(value = "SELECT * FROM PERFORMERS WHERE name LIKE %:name%", nativeQuery = true)
    Optional<List<PerformerDTO>> searchByPerformerName(@Param("name") String name);

}
