package musicEventsNearMe.dto;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "geo_coordinates")
public class GeoCoordinate extends Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "Geo_ID")
    private Long geo_id;
    private double latitude;
    private double longitude;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "geo")
    @PrimaryKeyJoinColumn
    @JsonIgnoreProperties("geo") // Exclude geo field from serialization
    private List<LocationDTO> location;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GeoCoordinate that = (GeoCoordinate) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
