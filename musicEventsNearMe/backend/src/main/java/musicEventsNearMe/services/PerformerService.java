package musicEventsNearMe.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.repositories.PerformerRepository;

@Service
public class PerformerService {
    @Autowired
    PerformerRepository performerRepository;

    public List<Long> savePerformersAndReturnIds(List<PerformerDTO> performers) {
        List<Long> list = new ArrayList<Long>();
        for (PerformerDTO performer : performers) {
            long index = checkForDuplicateData(performer);
            if (index == -1l) {
                index = performerRepository.saveAndFlush(performer).getId();
            }
            list.add(index);
        }
        return list;
    }

    public long checkForDuplicateData(PerformerDTO performer) {
        Optional<PerformerDTO> found = performerRepository.findByIdentifier(performer.getIdentifier());
        if (found.isPresent()) {
            return found.get().getId();
        }
        return -1l;
    }
}
