package musicEventsNearMe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import musicEventsNearMe.baseRepositories.PerformanceRepository;
import musicEventsNearMe.dto.PerformanceDTO;

@Service
public class PerformanceService {

    @Autowired
    private PerformanceRepository performanceRepository;

    public boolean checkIfEntityExists(Long musicEventId, Long performerId) {
        return performanceRepository.findByMusicEventIdAndPerformerId(musicEventId, performerId).isPresent();
    }

    public PerformanceDTO saveEntity(PerformanceDTO performanceDTO) {
        return performanceRepository.saveAndFlush(performanceDTO);
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
