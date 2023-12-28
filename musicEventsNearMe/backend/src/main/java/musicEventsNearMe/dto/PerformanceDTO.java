package musicEventsNearMe.dto;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "performances")
public class PerformanceDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PerformerDTO performer;

    @ManyToOne
    private MusicEventDTO musicEvent;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PerformanceDTO that = (PerformanceDTO) o;
        return Objects.equals(musicEvent.getId(), that.musicEvent.getId()) &&
                Objects.equals(performer.getId(), that.performer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicEvent.getId(), performer.getId());
    }
}
