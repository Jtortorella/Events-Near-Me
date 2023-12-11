import React, { createContext, useState, useEffect, ReactNode } from "react";
import { EventInfo, EventApiResponse, GeoCoordinates } from "../Interfaces/EventApiResponse";
import { get } from "../Services/APIService";
import DebuggerService from "../Services/DebuggerService"
import { RemoveDuplicatesBetweenTwoArrays } from "../Services/CheckForDuplicates";
export interface ConcertDataContextProps {
  events: EventInfo[] | null;
  geoLocationOfEvents: GeoCoordinates[] | null;
  isLoading: boolean;
  isError: boolean;
  setIsError: any;
}

interface ProviderProps {
  children: ReactNode;
}

// Use ConcertDataContextProps as the type for createContext
const Context = createContext<ConcertDataContextProps | undefined>(undefined);

const Provider: React.FC<ProviderProps> = ({ children }) => {
  let debuggerService = new DebuggerService('UseContext');

  const [events, setEvents] = useState<EventInfo[] | null>(null);
  const [geoLocationOfEvents, setGeoLocationOfEvents] = useState<GeoCoordinates[] | null>(null);

  const [isLoading, setisLoading] = useState<boolean>(true);
  const [isError, setIsError] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const eventResponse: EventApiResponse = await get({url: "http://localhost:8080/concertData/events"});
        if (events && events?.length > 0) {
          setEvents(RemoveDuplicatesBetweenTwoArrays(eventResponse.events, events));
        }
        else {
          setEvents(eventResponse.events);
        }
        if (geoLocationOfEvents && geoLocationOfEvents?.length > 0) {
          setEvents(RemoveDuplicatesBetweenTwoArrays(geoLocationOfEvents, handleSetGeoLocation(eventResponse.events)));
        }
        else {
          setGeoLocationOfEvents(handleSetGeoLocation(eventResponse.events));
        }
        debuggerService.showVerbose(eventResponse);
      } catch (err) {
        setIsError(true);
        debuggerService.showError(undefined, 'Could Not Connect To Database')
      } finally {
        setisLoading(false);
      }};
    fetchData();
  }, []);

  const contextValue: ConcertDataContextProps = {
    events,
    geoLocationOfEvents,
    isLoading,
    isError,
    setIsError,
  };
  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

function handleSetGeoLocation(events: EventInfo[]): GeoCoordinates[] {
  let locationArray: GeoCoordinates[] = [];
  for (let event of events) {
    locationArray.push(event.location.geo);
  }
  console.log(locationArray);
  return locationArray;
}

export { Context, Provider };


