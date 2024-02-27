package musicEventsNearMe.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import musicEventsNearMe.dto.Genre;

public class ObjectConverterOverrides {
    public Converter<List<String>, Set<Genre>> getGenreConverter() {
        return new Converter<List<String>, Set<Genre>>() {
            @Override
            public Set<Genre> convert(MappingContext<List<String>, Set<Genre>> context) {
                return context.getSource().stream().map((value) -> new Genre(null, value))
                        .collect(Collectors.toSet());
            }
        };
    }

    public Converter<String, LocalDateTime> getStartDateConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
                if (context.getSource() == null) {
                    return null;
                }
                String str = context.getSource();
                try {
                    return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } catch (DateTimeParseException e) {
                    return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                }
            }
        };
    }

    public Converter<String, LocalDateTime> getEndDateConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> context) {
                if (context.getSource() == null) {
                    return null;
                }
                String str = context.getSource();
                try {
                    return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                } catch (DateTimeParseException e) {
                    return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);
                }
            }
        };
    }
}
