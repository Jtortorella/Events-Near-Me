import React, { createContext, useState, ReactNode } from "react";
import {
  Filter,
  GeoCoordinates,
} from "../Interfaces/AppInterfaces";
export interface ConcertDataContextProps {
  filter: Filter;
  setFilter: any;
  locations: GeoCoordinates[] | null;
  handleGeoCoordinatesImport: any;
  isLoading: boolean;
  setIsLoading: any;
  isError: boolean;
  setIsError: any;
  setSelectedEventId: any;
  selectedEventId: any;
  keyWordSearchResponse: any;
  setKeyWordSearchResponse: any;
}

interface ProviderProps {
  children: ReactNode;
}

// Use ConcertDataContextProps as the type for createContext
const Context = createContext<ConcertDataContextProps | undefined>(undefined);

const Provider: React.FC<ProviderProps> = ({ children }) => {
  const [locations, setLocations] = useState<GeoCoordinates[] | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [selectedEventId, setSelectedEventId] = useState<
  number | number[]
>(-1);
const [keyWordSearchResponse, setKeyWordSearchResponse] = useState<string[]>([]);

  const [filter, setFilter] = useState<Filter>({
    startDate: new Date(),
    endDate: new Date(),
    reset: false,
  });

  const handleGeoCoordinatesImport = (newCoordinates: GeoCoordinates[]) => {
    setLocations(newCoordinates);
  };

  const contextValue: ConcertDataContextProps = {
    filter,
    setFilter,
    locations,
    handleGeoCoordinatesImport,
    isLoading,
    setIsLoading,
    isError,
    setIsError,
    setSelectedEventId,
    selectedEventId,
    keyWordSearchResponse,
    setKeyWordSearchResponse
  };
  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export { Context, Provider };
