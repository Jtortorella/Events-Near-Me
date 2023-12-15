import React, { createContext, useState, ReactNode } from "react";
import { EventInfo } from "../Interfaces/AppInterfaces";
import { RemoveDuplicatesBetweenTwoArrays } from "../Services/CheckForDuplicates";
export interface ConcertDataContextProps {
  events: EventInfo[] | null;
  handleEventDataImport: any,
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
  

  const contextValue: ConcertDataContextProps = {
    events,
    handleEventDataImport,
    isLoading,
    setIsLoading,
    isError,
    setIsError,
  };
  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};


export { Context, Provider };


