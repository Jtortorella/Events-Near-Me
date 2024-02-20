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
  const [filter, setFilter] = useState<Filter>({
    startDate: new Date(),
    endDate: new Date(),
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
  };
  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};

export { Context, Provider };
