package musicEventsNearMe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import musicEventsNearMe.baseRepositories.PerformerRepository;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.MusicEvent.Performer;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class PerformerService {

    @Autowired
    private PerformerRepository performerRepository;

    @Autowired
    private DataUtilities dataUtilities;

    public long checkForDuplicateData(PerformerDTO performerDTO) {
        return performerRepository.findByIdentifier(performerDTO.getIdentifier())
                .map(PerformerDTO::getId)
                .orElse(-1L);
    }

    public PerformerDTO findById(Long id) {
        return performerRepository.findById(id).orElse(null);
    }

    public boolean checkIfEntityExists(Performer performer) {
        PerformerDTO performerDTO = dataUtilities.getDTOEntityFromObject(performer, PerformerDTO.class);
        return performerRepository.findByIdentifier(performerDTO.getIdentifier()).isPresent();
    }

    public Long saveEntityAndReturnId(PerformerDTO performerDTO) {
        return performerRepository.saveAndFlush(performerDTO).getId();
    }

    public List<PerformerDTO> getExistingPerformers(List<Performer> performers) {
        return performers.stream()
                .map(this::getExistingPerformer)
                .collect(Collectors.toList());
    }

    public PerformerDTO getExistingPerformer(Performer performer) {
        return performerRepository.findByIdentifier(performer.getIdentifier()).orElse(null);
    }

    public List<PerformerDTO> getPerformersDTO(List<Performer> performers) {
        return performers.stream()
                .map(this::getPerformerDTO)
                .collect(Collectors.toList());
    }

    public PerformerDTO getPerformerDTO(Performer performer) {
        return dataUtilities.getDTOEntityFromObject(performer, PerformerDTO.class);
    }

    public Long updateEntityAndReturnId(PerformerDTO newPerformerDTO, PerformerDTO oldPerformerDTO) {
        return dataUtilities.updateEntity(newPerformerDTO, oldPerformerDTO, performerRepository).getId();
    }
}
