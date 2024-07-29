import React, {
  createContext,
  useState,
  ReactNode,
  useEffect,
  useContext,
} from "react";
import {
  EventInfo,
  Filter,
  GeoCoordinates,
  KeyWordResult,
} from "../Interfaces/AppInterfaces";
import {
  getEventDetails,
  getEventMarkersWithFilter,
  getEventMarkersWithKeyWord,
  getEventMarkersWithKeyWordAndMapBounds,
} from "../Hooks/APICall";
import { sortByDistanceFromCenter } from "../Services/DistanceService";
import { LatLngBounds, LatLngExpression, LatLng } from "leaflet";

interface ConcertDataContextProps {
  filter: Filter;
  setFilter: React.Dispatch<React.SetStateAction<Filter>>;
  locations: GeoCoordinates[] | null;
  isLoading: boolean;
  setIsLoading: React.Dispatch<React.SetStateAction<boolean>>;
  isError: boolean;
  setIsError: React.Dispatch<React.SetStateAction<boolean>>;
  selectedEventId: number | number[];
  setSelectedEventId: React.Dispatch<React.SetStateAction<number | number[]>>;
  keyWordSearchResponse: KeyWordResult[];
  setKeyWordSearchResponse: React.Dispatch<
    React.SetStateAction<KeyWordResult[]>
  >;
  infoArr: EventInfo[];
  setInfoArr: React.Dispatch<React.SetStateAction<EventInfo[]>>;
  mapBounds: LatLngBounds | undefined;
  setMapBounds: React.Dispatch<React.SetStateAction<LatLngBounds | undefined>>;
  activeKeyWord: KeyWordResult | undefined;
  setActiveKeyWord: React.Dispatch<
    React.SetStateAction<KeyWordResult | undefined>
  >;
  mapCenter: any;
  setMapCenter: any;
  errorMessage: any;
  setErrorMessage: any;
  setSearchInAllEvents: any;
  setLocations: any;
  setNavigatedToLocation: any;
  searchAllEvents: any;
}

interface ProviderProps {
  children: ReactNode;
}

const ConcertDataContext = createContext<ConcertDataContextProps | undefined>(
  undefined
);

const ConcertDataProvider: React.FC<ProviderProps> = ({ children }) => {
  const [locations, setLocations] = useState<GeoCoordinates[]>([]);
  const [searchAllEvents, setSearchInAllEvents] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [isError, setIsError] = useState<boolean>(false);
  const [selectedEventId, setSelectedEventId] = useState<number | number[]>(-1);
  const [activeKeyWord, setActiveKeyWord] = useState<KeyWordResult | undefined>(
    undefined
  );


  
  const [navigatedToLocation, setNavigatedToLocation] =
    useState<boolean>(false);
  const [keyWordSearchResponse, setKeyWordSearchResponse] = useState<
    KeyWordResult[]
  >([]);
  const [infoArr, setInfoArr] = useState<EventInfo[]>([]);
  const [mapBounds, setMapBounds] = useState<LatLngBounds | undefined>(
    undefined
  );
  const [filter, setFilter] = useState<Filter>({
    startDate: new Date(),
    endDate: new Date(),
    reset: false,
  });
  const [mapCenter, setMapCenter] = useState<LatLngExpression | undefined>(
    new LatLng(32.7765, -79.9311)
  );
  const [errorMessage, setErrorMessage] = useState<string | undefined>(
    undefined
  );




  function checkIfLocations() {
    if (locations != null) {
      if (locations.length == 0) {
        setInfoArr([]);
        setIsError(true);
        setErrorMessage(
          "Wow that's a pretty unique event, no event was found with the current times selected"
        );
      } else {
        setIsLoading(true);
        if (activeKeyWord != undefined) {
          if (
            activeKeyWord.keyWordType == "ADDRESS" ||
            activeKeyWord.keyWordType == "LOCATION"
          ) {
            if (locations.length > 0) {
              setMapCenter(
                new LatLng(locations[0].latitude, locations[0].longitude)
              );
            }
          }
        }
        setIsLoading(false);
      }
    }
  }

  useEffect(() => {
    const fetchEventInfo = async () => {
      if (selectedEventId != -1) {
        const fetchData = async () => {
          try {
            const resolvedInfo = await getEventDetails(selectedEventId);
            if (resolvedInfo) {
              setInfoArr(resolvedInfo);
            }
          } catch (error) {
            console.error("Error fetching event info:", error);
          }
        };
        fetchData();
      }
    };
    fetchEventInfo();
  }, [selectedEventId]);

  useEffect(() => {
    const fetchEventMarkers = async () => {
      if (activeKeyWord == undefined) {
        if (isLoading == false) {

        setInfoArr([]);
        const res = sortByDistanceFromCenter(
          await getEventMarkersWithFilter(
            mapBounds,
            filter,
            setIsLoading,
            setIsError,
            setErrorMessage
          ),
          mapCenter
        );
        setLocations(res);
      }
    };
  }
    fetchEventMarkers();
  }, [mapBounds]);

  useEffect(() => {
    const fetchEventMarkers = async () => {
      let res;
      if (searchAllEvents && activeKeyWord != undefined) {
        console.log(isLoading);

        if (isLoading == false) {
          res = await getEventMarkersWithKeyWord(
            setIsLoading,
            setIsError,
            activeKeyWord,
            setErrorMessage
          );
          setLocations(res);
        }

      } else if (activeKeyWord != undefined) {
        if (isLoading == false) {
        res = await getEventMarkersWithKeyWordAndMapBounds(
          setIsLoading,
          setIsError,
          activeKeyWord,
          setErrorMessage,
          mapBounds,
          filter
        );
        setLocations(res);
      }
      } 
    };
    fetchEventMarkers();
  }, [filter, activeKeyWord]);

  useEffect(() => {
    checkIfLocations();
  }, [locations]);

  const contextValue: ConcertDataContextProps = {
    filter,
    setFilter,
    locations,
    isLoading,
    setIsLoading,
    isError,
    setIsError,
    selectedEventId,
    setSelectedEventId,
    keyWordSearchResponse,
    setKeyWordSearchResponse,
    infoArr,
    setInfoArr,
    mapBounds,
    setMapBounds,
    activeKeyWord,
    setActiveKeyWord,
    mapCenter,
    setMapCenter,
    errorMessage,
    setErrorMessage,
    setSearchInAllEvents,
    setLocations,
    setNavigatedToLocation,
    searchAllEvents
  };

  return (
    <ConcertDataContext.Provider value={contextValue}>
      {children}
    </ConcertDataContext.Provider>
  );
};

const useConcertData = () => {
  const context = useContext(ConcertDataContext);
  if (!context) {
    throw new Error("useConcertData must be used within a ConcertDataProvider");
  }
  return context;
};

export { ConcertDataProvider, useConcertData, ConcertDataContext };
