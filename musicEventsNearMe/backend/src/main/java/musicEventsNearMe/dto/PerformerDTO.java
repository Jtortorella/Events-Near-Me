package musicEventsNearMe.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import musicEventsNearMe.dto.LocationDTO.Place;
import musicEventsNearMe.dto.MusicEventDTO.ExternalIdentifier;

@Data
@Entity
@Table(name = "performers")
public class PerformerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String identifier;
    private String url;
    private String image;

    private String datePublished;
    private String dateModified;
    private String performerType;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PerformerDTO> member;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PerformerDTO> memberOf;

    @ManyToOne(cascade = CascadeType.ALL)
    private Place foundingLocation;

    private String foundingDate;

    @ElementCollection
    private List<String> genre;

    private List<Long> eventsId;

    private String bandOrMusician;
    private int numUpcomingEvents;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalIdentifier> externalIdentifiers;

    private String performanceDate;
    private int performanceRank;
    private boolean isHeadliner;
    private boolean dateIsConfirmed;
}