package musicEventsNearMe.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.MusicEvent.Performer;
import musicEventsNearMe.repositories.PerformerRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class PerformerService {
    @Autowired
    PerformerRepository performerRepository;

    @Autowired
    DataUtilities dataUtilities;

    public long checkForDuplicateData(PerformerDTO performer) {
        Optional<PerformerDTO> found = performerRepository.findByIdentifier(performer.getIdentifier());
        if (found.isPresent()) {
            return found.get().getId();
        }
        return -1l;
    }

    public PerformerDTO findById(Long id) {
        return performerRepository.findById(id).orElse(null);
    }

    public boolean checkIfEntityExists(Performer performer) {
        return dataUtilities.checkForDuplicateDataAndReturnBoolean(
                dataUtilities.getDTOEntityFromObject(performer, PerformerDTO.class), performerRepository);
    }

    public boolean checkIfEntityNeedsUpdating(PerformerDTO newPerformer, PerformerDTO oldPerformer) {
        return dataUtilities.shouldUpdate(newPerformer, oldPerformer);
    }

    public Long saveEntityAndReturnId(PerformerDTO Performer) {
        return performerRepository.saveAndFlush(Performer).getId();
    }

    public ArrayList<PerformerDTO> getExistingPerformers(List<Performer> performers) {
        ArrayList<PerformerDTO> list = new ArrayList<>();
        for (Performer performer : performers) {
            list.add(getExistingPerformer(performer));
        }
        return list;

    }

    public PerformerDTO getExistingPerformer(Performer Performer) {
        return performerRepository.findByIdentifier(Performer.getIdentifier()).orElse(null);
    }

    public ArrayList<PerformerDTO> getPerformersDTO(List<Performer> performers) {
        ArrayList<PerformerDTO> list = new ArrayList<>();
        for (Performer performer : performers) {
            list.add(getPerformerDTO(performer));
        }
        return list;
    }

    public PerformerDTO getPerformerDTO(Performer Performer) {
        return dataUtilities.getDTOEntityFromObject(Performer, PerformerDTO.class);
    }

    public Long updateEntityAndReturnId(PerformerDTO newPerformer, PerformerDTO oldPerformer) {
        return dataUtilities.updateEntity(newPerformer, oldPerformer, performerRepository).getId();
    }
}
