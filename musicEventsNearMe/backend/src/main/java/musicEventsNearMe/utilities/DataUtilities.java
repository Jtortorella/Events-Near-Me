package musicEventsNearMe.utilities;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import musicEventsNearMe.dto.MusicEventDTO;
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

}
