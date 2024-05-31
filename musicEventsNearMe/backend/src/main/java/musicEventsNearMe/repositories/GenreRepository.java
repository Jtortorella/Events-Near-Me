package musicEventsNearMe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import musicEventsNearMe.dto.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByGenreName(String genreName);

    @Query(value = "SELECT genre_name, ' GENRE' AS TYPE FROM GENRES WHERE genre_name LIKE %:name% LIMIT 2", nativeQuery = true)
    Optional<List<String>> searchByGenreName(@Param("name") String name);
}
