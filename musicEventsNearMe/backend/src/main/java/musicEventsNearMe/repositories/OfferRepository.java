package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.Offer;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Optional<Offer> findByIdentifierAndUrlAndName(String identifier, String url, String name);
}
