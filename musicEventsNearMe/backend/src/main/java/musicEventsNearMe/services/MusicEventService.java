package musicEventsNearMe.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import musicEventsNearMe.baseRepositories.MusicEventRepository;
import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.dto.MusicEventDTO.Offer;
import musicEventsNearMe.dto.MusicEventDTO.PriceSpecification;
import musicEventsNearMe.dto.MusicEventDTO.Seller;
import musicEventsNearMe.entities.GeoCoordinatesResponeObject;
import musicEventsNearMe.entities.MapMaxims;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.repositories.OfferRepository;
import musicEventsNearMe.repositories.PriceSpecificationRepository;
import musicEventsNearMe.repositories.SellerRepository;
import musicEventsNearMe.utilities.DataUtilities;

@Service
public class MusicEventService {

    @Autowired
    MusicEventRepository musicEventRepository;

    @Autowired
    OfferRepository offerRepository;

    @Autowired
    PriceSpecificationRepository priceSpecificationRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    DataUtilities dataUtilities;

    @Autowired
    EntityManager entityManager;

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

    public ResponseEntity<Object> getEventDetailsById(Long id) {
        return ResponseEntity.ok().body(musicEventRepository.findEventDetailsById(id));
    }

    public LocalDateTime convertToLocalDateTime(String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(str, formatter);
    }

    public MusicEventDTO findById(Long id) {
        return musicEventRepository.findById(id).orElse(null);
    }

    @Transactional
    public MusicEventDTO saveEntityAndReturnEntity(MusicEventDTO event) {
        Set<Offer> uniqueOffers = new HashSet<Offer>();
        if (event.getOffers() != null && !event.getOffers().isEmpty()) {
            for (Offer offer : event.getOffers()) {
                offer.setPriceSpecification(saveOrUpdatePriceSpecification(offer.getPriceSpecification()));
                offer.setSeller(saveOrUpdateSeller(offer.getSeller()));
                uniqueOffers.add(saveOrUpdateOffer(offer));
            }
        }
        event.setOffers(uniqueOffers);
        return musicEventRepository.save(event);
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

    private Offer saveOrUpdateOffer(Offer offer) {
        return offerRepository.findByIdentifierAndUrlAndName(offer.getIdentifier(), offer.getUrl(),
                offer.getName()).orElseGet(() -> offerRepository.save(offer));
    }

    public MusicEventDTO getExistingMusicEvent(MusicEvent event) {
        return musicEventRepository.findByIdentifier(event.getIdentifier()).orElse(null);
    }

    public MusicEventDTO getMusicEventDTO(MusicEvent event) {
        return dataUtilities.getDTOEntityFromObject(event, MusicEventDTO.class);
    }

    public void updateEntity(MusicEventDTO newEvent, MusicEventDTO oldEvent) {
        // dataUtilities.updateEntity(newEvent, oldEvent, musicEventRepository);
        if (!newEvent.equals(oldEvent)) {

        }
        if (!newEvent.getOffers().equals(oldEvent.getOffers())) {

        }
    }

}
