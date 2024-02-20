package musicEventsNearMe.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import musicEventsNearMe.baseRepositories.MusicEventRepository;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.Offer;
import musicEventsNearMe.dto.PriceSpecification;
import musicEventsNearMe.dto.Seller;
import musicEventsNearMe.entities.GeoCoordinatesResponeObject;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.repositories.OfferRepository;
import musicEventsNearMe.repositories.PriceSpecificationRepository;
import musicEventsNearMe.repositories.SellerRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
@AllArgsConstructor
public class MusicEventService {

    private final MusicEventRepository musicEventRepository;
    private final OfferRepository offerRepository;
    private final PriceSpecificationRepository priceSpecificationRepository;
    private final SellerRepository sellerRepository;
    private final DataUtilities dataUtilities;

    public ResponseEntity<List<GeoCoordinatesResponeObject>> getConcertDataForMap(MapMaxims mapMaxim) {
        mapMaxim.checkForNegatives();
        return ResponseEntity.ok().body(
                musicEventRepository.findBetweenGeoCoordinates(
                        mapMaxim.getLatitudeLow(), mapMaxim.getLatitudeHigh(),
                        mapMaxim.getLongitudeLow(), mapMaxim.getLongitudeHigh(),
                        convertToLocalDateTime(mapMaxim.getStartDate()), convertToLocalDateTime(mapMaxim.getEndDate()))
                        .stream()
                        .map(response -> new GeoCoordinatesResponeObject((Long) response[0], (double) response[1],
                                (double) response[2]))
                        .collect(Collectors.toList()));
    }

    public ResponseEntity<MusicEventDTO> getEventDetailsById(Long id) {
        return id == null
                ? null
                : ResponseEntity.ok().body(musicEventRepository.findById(id).orElse(null));
    }

    public LocalDateTime convertToLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }

    @Transactional
    public MusicEventDTO saveEntityAndReturnEntity(MusicEventDTO event) {
        List<Offer> uniqueOffers = new ArrayList<Offer>();
        if (event.getOffers() != null && !event.getOffers().isEmpty()) {
            for (Offer offer : event.getOffers()) {
                offer.setPriceSpecification(saveOrUpdatePriceSpecification(offer.getPriceSpecification()));
                offer.setSeller(saveOrUpdateSeller(offer.getSeller()));
                uniqueOffers.add(saveOrUpdateOffer(offer));
            }
            event.setOffers(uniqueOffers);
        } else {
            event.setOffers(Collections.emptyList());
        }
        return musicEventRepository.save(event);
    }

    private Offer saveOrUpdateOffer(Offer offer) {
        return offerRepository.findByIdentifierAndUrlAndName(offer.getIdentifier(), offer.getUrl(),
                offer.getName()).orElseGet(() -> offerRepository.save(offer));
    }

    private PriceSpecification saveOrUpdatePriceSpecification(PriceSpecification priceSpecification) {
        return priceSpecificationRepository
                .findByPriceAndPriceCurrency(priceSpecification.getPrice(), priceSpecification.getPriceCurrency())
                .orElseGet(() -> priceSpecificationRepository.save(priceSpecification));
    }

    private Seller saveOrUpdateSeller(Seller seller) {
        return sellerRepository.findByIdentifierAndName(seller.getIdentifier(), seller.getName())
                .orElseGet(() -> sellerRepository.save(seller));
    }

    public Optional<MusicEventDTO> getExistingMusicEvent(MusicEvent event) {
        return musicEventRepository.findByIdentifier(event.getIdentifier());
    }

    public MusicEventDTO getMusicEventDTO(MusicEvent event) {
        return dataUtilities.getDTOEntityFromObject(event, MusicEventDTO.class);
    }

    public MusicEventDTO updateEntityAndReturnEntity(MusicEventDTO oldEvent) {
        return oldEvent;
    }

}
