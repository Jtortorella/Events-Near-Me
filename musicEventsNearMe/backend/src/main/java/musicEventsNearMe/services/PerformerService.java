package musicEventsNearMe.services;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import musicEventsNearMe.baseRepositories.PerformerRepository;
import musicEventsNearMe.dto.Genre;
import musicEventsNearMe.dto.PerformerDTO;
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
        Set<Genre> genres = new HashSet<Genre>();
        for (Genre genre : performerDTO.getGenres()) {
            genres.add(saveOrReturnPreviouslySavedGenre(genre));
        }
        performerDTO.setGenres(genres);
        return performerRepository.saveAndFlush(performerDTO);
    }

    private Genre saveOrReturnPreviouslySavedGenre(Genre genre) {
        return genreRepository
                .findByGenreName(
                        genre.getGenreName())
                .orElseGet(() -> genreRepository.saveAndFlush(genre));
    }

    public Optional<PerformerDTO> getExistingPerformer(Performer currentPerformer) {
        return performerRepository.findByIdentifier(currentPerformer.getIdentifier());
    }

    public PerformerDTO getPerformerDTO(Performer performer) {
        return dataUtilities.getDTOEntityFromObject(performer, PerformerDTO.class);
    }

    public PerformerDTO updateEntityAndReturnEntity(PerformerDTO oldPerformerDTO, PerformerDTO newPerformerDTO) {
        return oldPerformerDTO;
    }
}
