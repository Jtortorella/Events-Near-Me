import React, { createContext, useState, ReactNode } from "react";
import { EventInfo, Filter, GeoCoordinates } from "../Interfaces/AppInterfaces";
import { RemoveDuplicatesBetweenTwoArrays } from "../Services/CheckForDuplicates";
export interface ConcertDataContextProps {
  filter: Filter;
  setFilter: any;
  events: EventInfo[] | null;
  locations: GeoCoordinates[] | null;
  handleEventDataImport: any;
  handleGeoCoordinatesImport: any;
  isLoading: boolean;
  setIsLoading: any;
  isError: boolean;
  setIsError: any;
}

interface ProviderProps {
  children: ReactNode;
}

// Use ConcertDataContextProps as the type for createContext
const Context = createContext<ConcertDataContextProps | undefined>(undefined);

const Provider: React.FC<ProviderProps> = ({ children }) => {
  const [events, setEvents] = useState<EventInfo[] | null>(null);
  const [locations, setLocations] = useState<GeoCoordinates[] | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [filter, setFilter] = useState<Filter>({
    startDate: new Date(),
    endDate: new Date(),
  });

  const handleEventDataImport = (newEvents: EventInfo[]) => {
    setEvents((prevEvents: EventInfo[] | null) => {
      if (prevEvents) {
        return RemoveDuplicatesBetweenTwoArrays(newEvents, prevEvents);
      } else {
        return newEvents;
      }
    });
  };

  const handleGeoCoordinatesImport = (
    newCoordinates: GeoCoordinates[]
  ) => {
    setLocations(newCoordinates); 
  };

  const contextValue: ConcertDataContextProps = {
    filter,
    setFilter,
    events,
    locations,
    handleEventDataImport,
    handleGeoCoordinatesImport,
    isLoading,
    setIsLoading,
    isError,
    setIsError,
  };
  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export { Context, Provider };
