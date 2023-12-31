package musicEventsNearMe.interfaces;

import java.time.LocalDateTime;

public interface BaseEntity {
    Long getId();

    void setId(Long id);

    String getIdentifier();

    LocalDateTime getTimeRecordWasEntered();

    void setTimeRecordWasEntered(LocalDateTime timeRecordWasEntered);

    String getDatePublished();

    void setDatePublished(String datePublished);

    String getDateModified();

    void setDateModified(String dateModified);
}
