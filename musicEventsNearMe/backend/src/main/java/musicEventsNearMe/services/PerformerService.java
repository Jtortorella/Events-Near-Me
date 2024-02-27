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
import musicEventsNearMe.repositories.GenreRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
@AllArgsConstructor
public class PerformerService {

    private final PerformerRepository performerRepository;
    private final DataUtilities dataUtilities;
    private final GenreRepository genreRepository;

    public PerformerDTO saveEntityAndReturnEntity(PerformerDTO performerDTO) {
        return performerRepository.saveAndFlush(setEntityAndReturnEntity(performerDTO));
    }

    public PerformerDTO setEntityAndReturnEntity(PerformerDTO performerDTO) {
        if (performerDTO.getGenres() != null) {
            performerDTO.setGenres(performerDTO
                    .getGenres()
                    .stream()
                    .map(this::saveOrReturnPreviouslySavedGenre)
                    .collect(Collectors.toSet()));
        }
        return performerDTO;
    }

    private Genre saveOrReturnPreviouslySavedGenre(Genre genre) {
        return genreRepository
                .findByGenreName(
                        genre.getGenreName())
                .orElseGet(() -> genreRepository.saveAndFlush(genre));
    }

    public Optional<PerformerDTO> getExistingPerformer(PerformerDTO importedPerformerFromAPI) {
        return performerRepository.findByIdentifier(importedPerformerFromAPI.getIdentifier());
    }

    public List<PerformerDTO> updateOrSaveEntityAndReturnEntity(List<PerformerDTO> performerList) {
        return !performerList.isEmpty() ? performerList.stream()
                .map(performer -> getExistingPerformer(performer)
                        .map(existingPerformer -> {
                            PerformerDTO entity = setEntityAndReturnEntity(performer);
                            return existingPerformer.equals(entity) ? existingPerformer
                                    : dataUtilities.updateEntity(entity, existingPerformer,
                                            performerRepository);
                        })
                        .orElseGet(() -> saveEntityAndReturnEntity(performer)))
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

}
