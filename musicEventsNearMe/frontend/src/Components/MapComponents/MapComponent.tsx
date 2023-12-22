import { useContext, useEffect, useRef, useState } from "react";
import { GoogleMap, InfoWindow, Marker } from "@react-google-maps/api";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import {
  EventInfo,
  Filter,
  GeoCoordinates,
  MarkerInformation,
} from "../../Interfaces/AppInterfaces";
import { get } from "../../Services/APIService";
import { MapBounds } from "../../Interfaces/MapBounds";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { mapOptions } from "../../Styles/MapOptions";
import InfoWindowContent from "./InfoWindowContent";
import { mapContainerStyles } from "../../Styles/MapStyles";

const MapComponent = () => {
  const { events, handleEventDataImport, setIsLoading }: any = useContext(Context);
  const mapRef = useRef<google.maps.Map | undefined>(undefined);

  const [filter, setFilter] = useState<Filter | undefined>(undefined);
  const [filterSet, setFilterSet] = useState<Boolean>(false);
  const [filteredMarkers, setFilteredMarkers] = useState<MarkerInformation[] | undefined>(undefined);
  const [selectedMarker, setSelectedMarker] = useState<MarkerInformation | undefined>(undefined);
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng | undefined>(undefined);
  const [markers, setMarkers] = useState<MarkerInformation[]>([]);
  const [infoWindowDisplaying, setInfoWindowDisplaying] = useState<boolean>(false);
  const [geoLocationOfEvents, setGeoLocationOfEvents] = useState<GeoCoordinates[]>([]);


  useEffect(() => {
    setIsLoading(true);
    handleDefaultCenter();
  }, []);

  useEffect(() => {
    createMarkers();
    setIsLoading(false);
  }, [events]);

  useEffect(() => {
    setIsLoading(true);
    if (filter) {
      setFilteredMarkers(filterMarkerByEvents(filterEventsByFilter(events)));
      setFilterSet(true);
    }
  }, [filter]);

  useEffect(() => {
    updateMarkers();
    setIsLoading(false);
  }, [filter, filteredMarkers]);

  const setMapReferenceToCurrentMap = (event: google.maps.Map) => {
    mapRef.current = event;
  };

  const checkIfDateIsInBetweenTwoOtherDates = (
    compareDate: string,
    minimumDate: string,
    maximumDate: string
  ) => {
    const compareDateTime = new Date(compareDate).getTime();
    const minimumDateTime = new Date(minimumDate).getTime();
    const maximumDateTime = new Date(maximumDate).getTime();
    return compareDateTime >= minimumDateTime && compareDateTime <= maximumDateTime;
  };

  const filterMarkerByEvents = (eventsPassed: EventInfo[]) => {
    return [...markers].map((marker: MarkerInformation) => ({
      ...marker,
      event: marker.event.filter((event: EventInfo) =>
        eventsPassed.some((passedEvent: EventInfo) =>
          passedEvent.name.toLowerCase() === event.name.toLowerCase() &&
          passedEvent.startDate === event.startDate
        )
      ),
    })).filter((marker: MarkerInformation) => marker.event.length > 0);
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
      return filteredData;
    }
    return [];
  };

  const handleSetGeoLocation = (
    event: EventInfo,
    currentGeoLocationOfEvents: GeoCoordinates[]
  ) => {
    return currentGeoLocationOfEvents
      ? [...currentGeoLocationOfEvents, event.location.geo]
      : [event.location.geo];
  };

  const handleEditPreExistingMarker = (
    event: EventInfo,
    index: number,
    currentMarkers: MarkerInformation[]
  ) => {
    if (!currentMarkers || index < 0 || index >= currentMarkers.length) {
      return currentMarkers;
    }

    const updatedMarkers = [...currentMarkers];
    const currentEventsAtMarker = updatedMarkers[index].event;

    const eventExists = currentEventsAtMarker.some(
      (existingEvent) => existingEvent.identifier === event.identifier
    );

    if (!eventExists) {
      updatedMarkers[index] = {
        ...updatedMarkers[index],
        event: [...updatedMarkers[index].event, event],
      };
    }

    return updatedMarkers;
  };

  const checkIfLocationAlreadyExists = (
    location: GeoCoordinates,
    array: GeoCoordinates[]
  ) => {
    return array.findIndex(
      (eventLocation) =>
        eventLocation.latitude === location.latitude &&
        eventLocation.longitude === location.longitude
    );
  };

  const handleAddNewMarker = (
    event: EventInfo,
    currentMarkers: MarkerInformation[]
  ) => {
    const location = event.location.geo;
    const marker: MarkerInformation = {
      location: location,
      event: [event],
    };
    return currentMarkers ? [...currentMarkers, marker] : [marker];
  };

  const createMarkers = () => {
    let currentGeoLocationOfEvents = geoLocationOfEvents;
    let currentEvents = events;
    let currentMarkers = markers;

    for (const eventData of currentEvents || []) {
      const index = checkIfLocationAlreadyExists(
        eventData.location.geo,
        currentGeoLocationOfEvents
      );
      if (index !== -1) {
        currentMarkers = handleEditPreExistingMarker(
          eventData,
          index,
          currentMarkers
        );
      } else {
        currentGeoLocationOfEvents = handleSetGeoLocation(
          eventData,
          currentGeoLocationOfEvents
        );
        currentMarkers = handleAddNewMarker(eventData, currentMarkers);
      }
    }
    setMarkers(currentMarkers);
    setGeoLocationOfEvents(currentGeoLocationOfEvents);
  };

  const handleMapBoundChange = async () => {
    setIsLoading(true);
    const bounds = mapRef.current?.getBounds();
    if (bounds) {
      const northeast = bounds.getNorthEast();
      const southwest = bounds.getSouthWest();
      const mapBounds: MapBounds = {
        latitudeHigh: northeast.lat(),
        latitudeLow: southwest.lat(),
        longitudeHigh: northeast.lng(),
        longitudeLow: southwest.lng(),
      };
      try {
        const eventResponse: EventInfo[] | undefined = await get({
          url: `http://localhost:8080/concertData/events?latitudeHigh=${encodeURIComponent(
            mapBounds.latitudeHigh
          )}&latitudeLow=${encodeURIComponent(
            mapBounds.latitudeLow
          )}&longitudeLow=${encodeURIComponent(
            mapBounds.longitudeHigh
          )}&longitudeHigh=${encodeURIComponent(mapBounds.longitudeLow)}`,
        });
        handleEventDataImport(eventResponse);
      } catch (error) {
        console.error(error);
      }
    }
  };

  async function handleDefaultCenter(): Promise<
    google.maps.LatLng | undefined
  > {
    try {
      setMapCenter(await GetCurrentUsersPosition());
    } catch (error) {
      console.error("Error setting default centers", error);
      return undefined;
    }
  }

  const handleRemoveFilterClick = () => {
    setFilterSet(false);
  };

  const renderInfoWindow = (marker: MarkerInformation) => {
    return (
      <InfoWindow
        position={{
          lat: marker.location.latitude,
          lng: marker.location.longitude,
        }}
        onCloseClick={() => {
          setInfoWindowDisplaying(false);
          setSelectedMarker(undefined);
        }}
      >
        <InfoWindowContent eventInfo={marker.event} />
      </InfoWindow>
    );
  };

  const handleMarkerClick = (marker: MarkerInformation) => {
    setInfoWindowDisplaying(true);
    setSelectedMarker(marker);
  };

  const handleTimeFilterClick = (dayLength: number) => {
    const currentDate = new Date();
    const startDate = currentDate.toISOString().split("T")[0];

    const endDate = new Date(currentDate);
    endDate.setDate(currentDate.getDate() + dayLength - 1);
    const endDateString = endDate.toISOString().split("T")[0];

    setFilter({
      startDate: startDate,
      endDate: endDateString,
    });
  };

  const updateMarkers = () => {
    // Implement your logic for updating markers if needed
  };

  return (
    <>
      <button onClick={() => handleTimeFilterClick(1)}>Today</button>
      <button onClick={() => handleTimeFilterClick(3)}>Three Days</button>
      <button onClick={handleRemoveFilterClick}>Remove Filter</button>
      <div id="mapContainer" className="mapContainer">
        <GoogleMap
          onLoad={(map: google.maps.Map) => {
            setMapReferenceToCurrentMap(map);
          }}
          onBoundsChanged={handleMapBoundChange}
          mapContainerStyle={mapContainerStyles}
          center={mapCenter}
          zoom={10}
          options={mapOptions}
        >
          {filterSet === false
            ? markers.map((marker) => (
                <Marker
                  key={marker.event[0].identifier}
                  position={{
                    lat: marker.location.latitude,
                    lng: marker.location.longitude,
                  }}
                  onClick={() => handleMarkerClick(marker)}
                />
              ))
            : filteredMarkers!.map((marker) => (
                <Marker
                  key={marker.event[0].identifier}
                  position={{
                    lat: marker.location.latitude,
                    lng: marker.location.longitude,
                  }}
                  onClick={() => handleMarkerClick(marker)}
                />
              ))}
          {infoWindowDisplaying &&
            selectedMarker &&
            renderInfoWindow(selectedMarker)}
        </GoogleMap>
      </div>
    </>
  );
};

export default MapComponent;
