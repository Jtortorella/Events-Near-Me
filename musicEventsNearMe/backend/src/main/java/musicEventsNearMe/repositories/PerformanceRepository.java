package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.PerformanceDTO;
import musicEventsNearMe.dto.PerformerDTO;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<PerformanceDTO, Long> {
    List<PerformanceDTO> findByMusicEvent(MusicEventDTO musicEvent);

    List<PerformanceDTO> findByPerformer(PerformerDTO performer);

    Optional<PerformanceDTO> findByMusicEventIdAndPerformerId(Long musicEventId, Long performerId);

    List<PerformanceDTO> findByMusicEventId(Long musicEventId);

}