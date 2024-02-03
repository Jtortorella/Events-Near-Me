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
        List<Offer> offers = new ArrayList<>();
        if (event.getOffers() != null && !event.getOffers().isEmpty()) {
            Set<Offer> uniqueOffers = new HashSet<Offer>();
            for (Offer offer : event.getOffers()) {
                offer.setPriceSpecification(saveOrUpdatePriceSpecification(offer.getPriceSpecification()));
                offer.setSeller(saveOrUpdateSeller(offer.getSeller()));
                if (uniqueOffers.add(offer)) {
                    offers.add(saveOrUpdateOffer(offer));
                }
            }
        }
        event.setOffers(offers);
        return musicEventRepository.save(event);
    }

    private PriceSpecification saveOrUpdatePriceSpecification(PriceSpecification priceSpecification) {
        Optional<PriceSpecification> found = priceSpecificationRepository
                .findByPriceAndPriceCurrency(priceSpecification.getPrice(), priceSpecification.getPriceCurrency());
        if (!found.isPresent()) {
            return priceSpecificationRepository.save(priceSpecification);
        }
        return found.get();
    }

    private Seller saveOrUpdateSeller(Seller seller) {
        Optional<Seller> found = sellerRepository.findByIdentifierAndName(seller.getIdentifier(), seller.getName());
        if (!found.isPresent()) {
            return sellerRepository.save(seller);
        }
        return found.get();
    }

    private Offer saveOrUpdateOffer(Offer offer) {
        Optional<Offer> found = offerRepository.findByIdentifierAndUrlAndName(offer.getIdentifier(), offer.getUrl(),
                offer.getName());
        if (!found.isPresent()) {
            return offerRepository.save(offer);
        }
        return found.get();
    }

    public MusicEventDTO getExistingMusicEvent(MusicEvent event) {
        return musicEventRepository.findByIdentifier(event.getIdentifier()).orElse(null);
    }

    public MusicEventDTO getMusicEventDTO(MusicEvent event) {
        return dataUtilities.getDTOEntityFromObject(event, MusicEventDTO.class);
    }

    public void updateEntity(MusicEventDTO newEvent, MusicEventDTO oldEvent) {
        dataUtilities.updateEntity(newEvent, oldEvent, musicEventRepository);
    }

}
