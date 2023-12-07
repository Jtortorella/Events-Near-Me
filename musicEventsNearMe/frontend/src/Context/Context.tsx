import React, { createContext, useState, useEffect, ReactNode } from "react";
import { Event, EventApiResponse, GeoCoordinates } from "../Interfaces/EventApiResponse";
import { get } from "../Hooks/APIService";

export interface ConcertDataContextProps {
  events: Event[] | null;
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
  const [events, setEvents] = useState<Event[] | null>(null);
  const [geoLocationOfEvents, setGeoLocationOfEvents] = useState<GeoCoordinates[] | null>(null);

  const [isLoading, setisLoading] = useState<boolean>(true);
  const [isError, setIsError] = useState<boolean>(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const eventResponse: EventApiResponse = await get({url: "http://localhost:8080/concertData/events"});
        setEvents(eventResponse.events);
        setGeoLocationOfEvents(handleSetGeoLocation(eventResponse.events));
        console.log(eventResponse.events);
      } catch (err) {
        setIsError(true);
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

function handleSetGeoLocation(events: Event[]): GeoCoordinates[] {
  let locationArray: GeoCoordinates[] = [];
  for (let event of events) {
    locationArray.push(event.location.geo);
  }
  console.log(locationArray);
  return locationArray;
}

export { Context, Provider };


