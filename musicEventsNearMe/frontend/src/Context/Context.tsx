import React, { createContext, useState, ReactNode } from "react";
import { EventInfo, GeoCoordinates } from "../Interfaces/AppInterfaces";
import { RemoveDuplicatesBetweenTwoArrays } from "../Services/CheckForDuplicates";
export interface ConcertDataContextProps {
  events: EventInfo[] | null,
  locations: GeoCoordinates[] | null,
  handleEventDataImport: any,
  handleGeoCoordinatesImport: any,
  isLoading: boolean;
  setIsLoading: any,
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


  
  const handleEventDataImport = (newEvents: EventInfo[]) => {
    setEvents((prevEvents: EventInfo[] | null) => {
      if (prevEvents) {
        return RemoveDuplicatesBetweenTwoArrays(newEvents, prevEvents);
      }
      else {
        return newEvents;
      }
    });
  };

  const handleGeoCoordinatesImport = (newCoordinates: GeoCoordinates[]) => {
    setLocations((prevCoordinates: GeoCoordinates[] | null) => {
      if (prevCoordinates) {
        return RemoveDuplicatesBetweenTwoArrays(newCoordinates, prevCoordinates);
      }
      else {
        return newCoordinates;
      }
    });
  };
  

  const contextValue: ConcertDataContextProps = {
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


