import React, { useState } from 'react'
import { EventInfo, oldMarkerInformation, GeoCoordinates, Filter } from '../../Interfaces/AppInterfaces';

export default function OldFunctionsForMapComponent() {
    const [filter, setFilter] = useState<Filter | undefined>(undefined);
  // useEffect(() => {
  //   if (filter) {
      // setFilteredMarkers(filterMarkerByEvents(filterEventsByFilter(events)));
  //     setFilterSet(true);
  //   }
  // }, [filter, events]);
    const oldFilterMarkerByEvents = (eventsPassed: EventInfo[]) => {
        return [].map((marker: oldMarkerInformation) => ({
          ...marker,
          event: marker.event.filter((event: EventInfo) =>
            eventsPassed.some((passedEvent: EventInfo) =>
              passedEvent.name.toLowerCase() === event.name.toLowerCase() &&
              passedEvent.startDate === event.startDate
            )
          ),
        })).filter((marker: oldMarkerInformation) => marker.event.length > 0);
      };
    
      const filterEventsByFilter = (eventsPassed: EventInfo[]) => {
        let filteredData: EventInfo[] = [];
        if (filter) {
          if (eventsPassed) {
            filteredData = eventsPassed.filter((event: EventInfo) => {
              return checkIfDateIsInBetweenTwoOtherDates(
                new Date(event.startDate).toISOString().split("T")[0],
                filter.startDate,
                filter.endDate
              );
            });
          }
          console.log(filteredData);
          return filteredData;
        }
        return [];
      };
    
      const oldHandleSetGeoLocation = (
        event: EventInfo,
        currentGeoLocationOfEvents: GeoCoordinates[]
      ) => {
        return currentGeoLocationOfEvents
          ? [...currentGeoLocationOfEvents, event.location.geo]
          : [event.location.geo];
      };
}
function checkIfDateIsInBetweenTwoOtherDates(arg0: string, startDate: any, endDate: any): unknown {
    throw new Error('Function not implemented.');
}

