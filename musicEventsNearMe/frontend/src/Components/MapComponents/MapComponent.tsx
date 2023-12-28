import { SetStateAction, useContext, useEffect, useRef, useState } from "react";
import { GoogleMap, InfoWindow, Marker } from "@react-google-maps/api";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import {
  EventInfo,
  Filter,
  GeoCoordinates,
  MarkerInformation,
  oldMarkerInformation,
} from "../../Interfaces/AppInterfaces";
import { get } from "../../Services/APIService";
import { MapBounds } from "../../Interfaces/MapBounds";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { mapOptions } from "../../Styles/MapOptions";
import InfoWindowContent from "./InfoWindowContent";
import { mapContainerStyles } from "../../Styles/MapStyles";

const MapComponent = () => {
  const { events, handleEventDataImport, handleGeoCoordinatesImport, setIsLoading, locations }: any = useContext(Context);
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
  }, [locations]);

  useEffect(() => {
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
    const minimumDateTime = new Date(minimumDate).setUTCHours(0, 0, 0, 0);
    const maximumDateTime = new Date(maximumDate).setUTCHours(23, 59, 59, 999);  
    return compareDateTime > minimumDateTime && compareDateTime < maximumDateTime;
  };



  const handleSetGeoLocation = (
    location: GeoCoordinates,
    currentGeoLocationOfEvents: GeoCoordinates[]
  ) => {
    return currentGeoLocationOfEvents
      ? [...currentGeoLocationOfEvents, location]
      : [location];
  };



  const handleEditPreExistingMarker = (
    event: EventInfo,
    index: number,
    currentMarkers: oldMarkerInformation[]
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
    location: GeoCoordinates,
    currentMarkers: MarkerInformation[]
  ) => {
    const marker: MarkerInformation = {
      location: location,
    };
    return currentMarkers ? [...currentMarkers, marker] : [marker];
  };



  const createMarkers = () => {
    let currentGeoLocationOfEvents = geoLocationOfEvents;
    let currentMarkers = markers;
    for (const location of locations || []) {
      const index = checkIfLocationAlreadyExists(
        location,
        currentGeoLocationOfEvents
      );
      if (index === -1) {
        currentGeoLocationOfEvents = handleSetGeoLocation(
          location,
          currentGeoLocationOfEvents
        );
        currentMarkers = handleAddNewMarker(location, currentMarkers);
      }
    }
    setMarkers(currentMarkers);
    setGeoLocationOfEvents(currentGeoLocationOfEvents);
  };

  const oldCreateMarkers = () => {
    let currentGeoLocationOfEvents = geoLocationOfEvents;
    let currentEvents = events;
    let currentMarkers: oldMarkerInformation[] = [];
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
        currentMarkers = oldHandleAddNewMarker(eventData, currentMarkers);
      }
    }
    setMarkers(currentMarkers);
    setGeoLocationOfEvents(currentGeoLocationOfEvents);
  };
  const oldHandleAddNewMarker = (
    event: EventInfo,
    currentMarkers: oldMarkerInformation[]
  ) => {
    const location = event.location.geo;
    const marker: oldMarkerInformation = {
      location: location,
      event: [event],
    };
    return currentMarkers ? [...currentMarkers, marker] : [marker];
  };

  const handleMapBoundChange = async () => {
    setIsLoading(true);
    // Get the current date and time
  const currentDate = new Date();

  // Subtract 5 hours
    currentDate.setHours(currentDate.getHours() - 5);
    const bounds = mapRef.current?.getBounds();
    if (bounds) {
      const northeast = bounds.getNorthEast();
      const southwest = bounds.getSouthWest();
      const mapBounds: MapBounds = {
        latitudeHigh: northeast.lat(),
        latitudeLow: southwest.lat(),
        longitudeHigh: northeast.lng(),
        longitudeLow: southwest.lng(),
        startDate: currentDate.toISOString().slice(0, 19),
        endDate: filter?.endDate || new Date().toISOString().slice(0, 10) + 'T23:59:59',
      };
      try {
        console.log(mapBounds.endDate.toString());
        console.log(mapBounds.startDate.toString());
  
        const eventResponse: GeoCoordinates[] | undefined = await get({
          url: `http://localhost:8080/concertData/events?latitudeHigh=${encodeURIComponent(
            mapBounds.latitudeHigh
          )}&latitudeLow=${encodeURIComponent(
            mapBounds.latitudeLow
          )}&longitudeLow=${encodeURIComponent(
            mapBounds.longitudeHigh
          )}&longitudeHigh=${encodeURIComponent(
            mapBounds.longitudeLow
          )}&startDate=${encodeURI(
            mapBounds.startDate.toString()
          )}&endDate=${encodeURI(
            mapBounds.endDate.toString()
          )}`,
        });
        handleGeoCoordinatesImport(eventResponse);
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
    return null;
    // return (
    //   <InfoWindow
    //     position={{
    //       lat: marker.location.latitude,
    //       lng: marker.location.longitude,
    //     }}
    //     onCloseClick={() => {
    //       setInfoWindowDisplaying(false);
    //       setSelectedMarker(undefined);
    //     }}
    //   >
    //     <InfoWindowContent eventInfo={marker.event} />
    //   </InfoWindow>
    // );
  };

  const handleMarkerClick = (marker: MarkerInformation) => {
    // setInfoWindowDisplaying(true);
    // setSelectedMarker(marker);
  };

  const handleTimeFilterClick = (dayLength: number) => {
    const currentDate = new Date();
    console.log(currentDate);
    const startDate = currentDate.toISOString().split("T")[0];

    const endDate = new Date(currentDate);
    endDate.setDate(currentDate.getDate() + dayLength);
    const endDateString = endDate.toISOString().split("T")[0];

    setFilter({
      startDate: startDate,
      endDate: endDateString,
    });
  };


  return (
    <>
      {/* <button onClick={() => handleTimeFilterClick(1)}>Today</button>
      <button onClick={() => handleTimeFilterClick(3)}>Today</button>
      <button onClick={() => handleRemoveFilterClick()}>Remove Filter</button> */}
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
                  // key={marker.event[0].identifier}
                  position={{
                    lat: marker.location.latitude,
                    lng: marker.location.longitude,
                  }}
                  onClick={() => handleMarkerClick(marker)}
                />
              ))
            : filteredMarkers!.map((marker) => (
                <Marker
                  // key={marker.event[0].identifier}
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
