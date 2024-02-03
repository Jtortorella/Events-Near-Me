package musicEventsNearMe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import musicEventsNearMe.dto.MusicEventDTO.PriceSpecification;
import java.util.Optional;

public interface PriceSpecificationRepository extends JpaRepository<PriceSpecification, Long> {
    Optional<PriceSpecification> findByPriceAndPriceCurrency(double price, String priceCurrency);
}
