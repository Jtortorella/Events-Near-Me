package musicEventsNearMe.utilities;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.MusicEventDTO;
import musicEventsNearMe.entities.GeoCoordinatesResponseObject;
import musicEventsNearMe.entities.KeyWord;
import musicEventsNearMe.entities.MusicEvent;
import musicEventsNearMe.interfaces.BaseEntity;

@Service
public class DataUtilities {

    private final ModelMapper modelMapper = new ModelMapper();

    private final ObjectConverterOverrides overrides = new ObjectConverterOverrides();

    public <D, T> D getDTOEntityFromObject(T data, Type type) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (type == MusicEventDTO.class) {
            Converter<String, LocalDateTime> startDateConverter = overrides.getStartDateConverter();
            Converter<String, LocalDateTime> endDateConverter = overrides.getEndDateConverter();
            modelMapper.addConverter(startDateConverter);
            modelMapper.addConverter(endDateConverter);
            modelMapper.typeMap(MusicEvent.class, MusicEventDTO.class)
                    .addMappings(mapping -> {
                        mapping.using(startDateConverter)
                                .map(MusicEvent::getStartDate, MusicEventDTO::setStartDate);
                        mapping.using(endDateConverter).map(MusicEvent::getEndDate,
                                MusicEventDTO::setEndDate);
                    });
        }
        return modelMapper.map(data, type);
    }

    public <T extends BaseEntity> T updateEntity(T newEntity, T existingEntity, JpaRepository<T, Long> repository) {
        Long id = existingEntity.getId();
        modelMapper.map(newEntity, existingEntity);
        existingEntity.setTimeRecordWasEntered(LocalDateTime.now());
        existingEntity.setId(id);
        return repository.saveAndFlush(existingEntity);
    }

    public static Optional<List<KeyWord>> convertToKeyWordResponse(
            Optional<List<String[]>> rawSQLData) {
        if (rawSQLData.isPresent()) {
            List<String[]> entities = rawSQLData.get();
            List<KeyWord> keyWordResponses = new ArrayList<>();
            for (String[] row : entities) {
                if (row.length >= 2) {
                    keyWordResponses.add(new KeyWord(row[0], row[1]));
                }
            }
            return Optional.of(keyWordResponses);
        } else {
            return Optional.empty();
        }
    }

    public static List<GeoCoordinatesResponseObject> mapToGeoCoordinatesResponseObject(
            List<Object[]> betweenGeoCoordinates) {
        return betweenGeoCoordinates.stream()
                .map(response -> new GeoCoordinatesResponseObject((Long) response[0], (Double) response[1],
                        (Double) response[2]))
                .collect(Collectors.toList());
    }
}