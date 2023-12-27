package musicEventsNearMe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.PerformerDTO;

public interface PerformerRepository extends JpaRepository<PerformerDTO, Long> {
    Optional<PerformerDTO> findByIdentifier(String identifier);

}
