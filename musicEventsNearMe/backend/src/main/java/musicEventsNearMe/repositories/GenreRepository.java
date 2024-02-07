package musicEventsNearMe.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String genreName);
}
