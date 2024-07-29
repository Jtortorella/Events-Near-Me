package musicEventsNearMe.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import musicEventsNearMe.baseRepositories.PerformerRepository;
import musicEventsNearMe.dto.Genre;
import musicEventsNearMe.dto.PerformerDTO;
import musicEventsNearMe.entities.KeyWord;
import musicEventsNearMe.entities.MusicEvent.Performer;
import musicEventsNearMe.repositories.GenreRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
@AllArgsConstructor
public class PerformerService {

    private final PerformerRepository performerRepository;
    private final DataUtilities dataUtilities;
    private final GenreRepository genreRepository;

    public PerformerDTO saveEntityAndReturnEntity(PerformerDTO performerDTO) {
        return performerRepository.saveAndFlush(performerDTO);
    }

    public PerformerDTO setEntityAndReturnEntity(Performer performer) {
        PerformerDTO entity = dataUtilities.getDTOEntityFromObject(performer, PerformerDTO.class);
        if (performer.getGenre() != null) {
            entity.setGenres(performer
                    .getGenre()
                    .stream()
                    .map(this::saveOrReturnPreviouslySavedGenre)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private Genre saveOrReturnPreviouslySavedGenre(String genreName) {
        String formattedName = String.join(" ", genreName.split("-")).toUpperCase();
        return genreRepository.findByGenreName(formattedName)
                .orElseGet(() -> genreRepository.saveAndFlush(new Genre(null, formattedName)));
    }

    public Optional<PerformerDTO> getExistingPerformer(Performer importedPerformerFromAPI) {
        return performerRepository.findByIdentifier(importedPerformerFromAPI.getIdentifier());
    }

    public List<PerformerDTO> updateOrSaveEntityAndReturnEntity(List<Performer> performerList) {
        if (!performerList.isEmpty()) {
            return performerList.stream()
                    .map(performer -> getExistingPerformer(performer)
                            .map(existingPerformer -> {
                                PerformerDTO entity = setEntityAndReturnEntity(performer);
                                return existingPerformer.equals(entity)
                                        ? existingPerformer
                                        : dataUtilities.updateEntity(entity, existingPerformer, performerRepository);
                            })
                            .orElseGet(() -> saveEntityAndReturnEntity(setEntityAndReturnEntity(performer))))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public Optional<List<KeyWord>> searchForPerformer(String keyword) {
        return DataUtilities.convertToKeyWordResponse(performerRepository.searchByPerformerName(keyword));
    }

    public Optional<List<KeyWord>> searchForGenre(String keyword) {
        return DataUtilities.convertToKeyWordResponse(genreRepository.searchByGenreName(keyword));
    }

}
