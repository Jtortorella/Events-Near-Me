package musicEventsNearMe.utilities;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import musicEventsNearMe.interfaces.BaseEntity;
import musicEventsNearMe.interfaces.BaseRepository;

@Service
public class DataUtilities {

    private final ModelMapper modelMapper = new ModelMapper();

    public <D, T> D getDTOEntityFromObject(T data, Type type) {
        return modelMapper.map(data, type);
    }

    public <D, T> List<T> getDTOEntityFromObjectList(List<T> list, Type type) {
        TypeToken<List<T>> typeToken = new TypeToken<List<T>>() {
        };
        return modelMapper.map(list, typeToken.getType());
    }

    public <T extends BaseEntity> boolean shouldUpdate(T newData, T oldData) {
        if (oldData != null) {
            return compareDatabaseRecordEnteredTimeAndEventsTime(newData.getDateModified(),
                    oldData.getTimeRecordWasEntered()) &&
                    compareDatabaseRecordEnteredTimeAndEventsTime(newData.getDatePublished(),
                            oldData.getTimeRecordWasEntered());

        }
        return false;

    }

    private <T> boolean compareDatabaseRecordEnteredTimeAndEventsTime(String dt1, LocalDateTime dt2) {
        if (dt1 != null && dt2 != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
                LocalDateTime dateTimeOfEventTime = LocalDateTime.parse(dt1, formatter);
                Duration duration = Duration.between(dateTimeOfEventTime, dt2);
                return duration.isNegative();
            } catch (DateTimeParseException ex) {
                return false;
            }
        }
        return false;
    }

    public <T extends BaseEntity> T checkForDuplicateDataAndReturnEntity(T entity,
            BaseRepository<T> repository) {
        return repository.findByIdentifier(entity.getIdentifier()).orElse(null);
    }

    public <T extends BaseEntity> boolean checkForDuplicateDataAndReturnBoolean(T entity,
            BaseRepository<T> repository) {
        return repository.findByIdentifier(entity.getIdentifier()).isPresent();
    }

    public <T extends BaseEntity> T updateEntity(T newEvent, T event, JpaRepository<T, Long> repository) {
        if (newEvent != null) {
            modelMapper.map(event, newEvent);
            event.setTimeRecordWasEntered(LocalDateTime.now());
            return repository.saveAndFlush(newEvent);
        }
        return null;
    }

}
