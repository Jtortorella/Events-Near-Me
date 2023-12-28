package musicEventsNearMe.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.PerformanceDTO;
import musicEventsNearMe.repositories.PerformanceRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class PerformanceService {

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    DataUtilities dataUtilities;

    public boolean checkIfEntityExists(Long musicEventId, Long performerId) {
        return performanceRepository.findByMusicEventIdAndPerformerId(musicEventId, performerId).isPresent();
    }

    public PerformanceDTO saveEntity(PerformanceDTO event) {
        return performanceRepository.saveAndFlush(event);
    }

    public PerformanceDTO getExistingPerformance(Long musicEventId, Long performerId) {
        return performanceRepository.findByMusicEventIdAndPerformerId(musicEventId, performerId).orElse(null);
    }

    public List<PerformanceDTO> getAllPerformancesForAMusicEvent(Long musicEventId) {
        return performanceRepository.findByMusicEventId(musicEventId);
    }

    public void deleteEntity(Long performanceId) {
        performanceRepository.deleteById(performanceId);
    }

}
