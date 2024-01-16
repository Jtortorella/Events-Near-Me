import React, { useContext, useEffect, useRef, useState } from "react";
import { GoogleMap, InfoWindow, Marker } from "@react-google-maps/api";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import {
  EventInfo,
  GeoCoordinates,
  MarkerInformation,
} from "../../Interfaces/AppInterfaces";
import { get } from "../../Services/APIService";
import { MapBounds } from "../../Interfaces/MapBounds";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { mapOptions } from "../../Styles/MapOptions";
import { mapContainerStyles } from "../../Styles/MapStyles";
import InfoWindowContent from "./InfoWindowContent";

const MapComponent: React.FC = () => {
  const { handleGeoCoordinatesImport, setIsLoading, locations, filter }: any =
    useContext(Context);
  const mapRef = useRef<google.maps.Map | undefined>(undefined);
  const [selectedMarker, setSelectedMarker] = useState<
    MarkerInformation | undefined
  >(undefined);
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng | undefined>(
    undefined
  );
  const [markers, setMarkers] = useState<MarkerInformation[]>([]);
  const [infoWindowDisplaying, setInfoWindowDisplaying] =
    useState<boolean>(false);
  const [geoLocationOfEvents, setGeoLocationOfEvents] = useState<
    GeoCoordinates[]
  >([]);

  useEffect(() => {
    setIsLoading(true);
    handleDefaultCenter();
  }, []);

  useEffect(() => {
    createMarkers();
    setIsLoading(false);
  }, [locations]);

  useEffect(() => {
    setIsLoading(true);
    handleMapBoundChangeOrFilterChange();
    setInfoWindowDisplaying(false);
    setSelectedMarker(undefined);
    setIsLoading(false);
  }, [filter]);

  const setMapReferenceToCurrentMap = (event: google.maps.Map) => {
    mapRef.current = event;
  };

  const handleSetGeoLocation = (
    location: GeoCoordinates,
    currentGeoLocationOfEvents: GeoCoordinates[]
  ) => {
    return currentGeoLocationOfEvents
      ? [...currentGeoLocationOfEvents, location]
      : [location];
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
      musicEventId: location.musicEventId,
      location: location,
    };
    return currentMarkers ? [...currentMarkers, marker] : [marker];
  };

  const handleUpdateMarker = (
    location: GeoCoordinates,
    currentMarkers: MarkerInformation[]
  ) => {
    const index = currentMarkers.findIndex(
      (marker) =>
        marker.location.latitude === location.latitude &&
        marker.location.longitude === location.longitude
    );

    if (index !== -1) {
      let previousMusicEventId: number | number[] =
        currentMarkers[index].musicEventId;
      previousMusicEventId = Array.isArray(previousMusicEventId)
        ? [...previousMusicEventId, location.musicEventId]
        : [previousMusicEventId, location.musicEventId];
      currentMarkers[index] = {
        musicEventId: previousMusicEventId,
        location,
      };
    }
    return currentMarkers;
  };

  const createMarkers = () => {
    let currentGeoLocationOfEvents: GeoCoordinates[] = [];
    let currentMarkers: MarkerInformation[] = [];

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
      } else {
        currentMarkers = handleUpdateMarker(location, currentMarkers);
      }
    }

    setMarkers(currentMarkers);
    setGeoLocationOfEvents(currentGeoLocationOfEvents);
  };
  const convertStartDateTime = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-based
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}T00:00:00`;
  };
  const convertEndDateTime = (date: Date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are zero-based
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}T23:59:59`;
  };
  const handleMapBoundChangeOrFilterChange = async () => {
    setIsLoading(true);

    const currentDate = new Date();
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
        startDate: filter
          ? convertStartDateTime(filter.startDate)
          : convertStartDateTime(new Date()),
        endDate: filter
          ? convertEndDateTime(filter.endDate)
          : convertEndDateTime(new Date()),
      };

      try {
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
          )}&endDate=${encodeURI(mapBounds.endDate.toString())}`,
        });

        handleGeoCoordinatesImport(eventResponse);
      } catch (error) {
        console.error(error);
        setIsLoading(false);
      }
    }
  };

  async function getEventDetails(
    id: number | number[]
  ): Promise<EventInfo[] | undefined> {
    const url = `http://localhost:8080/concertData/event/`;

    if (Array.isArray(id)) {
      try {
        const responses = await Promise.all(
          id.map(async (eventId) => {
            try {
              return await get({ url: url.concat(eventId.toString()) });
            } catch (err) {
              console.error("Error fetching event details:", err);
              return null;
            }
          })
        );

        const validResponses = responses.filter(
          (response) => response !== null
        );
        return validResponses.length > 0 ? validResponses : undefined;
      } catch (err) {
        console.error("Error fetching event details:", err);
        return undefined;
      }
    } else if (Number.isInteger(id)) {
      try {
        const response = await get({ url: url.concat(id.toString()) });
        return Array.isArray(response) ? response : [response];
      } catch (err) {
        console.error("Error fetching event details:", err);
        return undefined;
      }
    }
    return undefined;
  }

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

  const renderInfoWindow = (marker: MarkerInformation) => {
    try {
      const eventDetails = getEventDetails(marker.musicEventId);

      if (eventDetails) {
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
            <InfoWindowContent eventInfo={eventDetails} />
          </InfoWindow>
        );
      } else {
        console.error(
          "Error fetching event details: Event details is undefined"
        );
        return null;
      }
    } catch (error) {
      console.error("Error fetching event details:", error);
      return null;
    }
  };

  const handleMarkerClick = (marker: MarkerInformation) => {
    setInfoWindowDisplaying(true);
    setSelectedMarker(marker);
  };

  return (
    <>
      <div id="mapContainer" className="mapContainer">
        <GoogleMap
          onLoad={(map: google.maps.Map) => {
            setMapReferenceToCurrentMap(map);
          }}
          onBoundsChanged={handleMapBoundChangeOrFilterChange}
          mapContainerStyle={mapContainerStyles}
          center={mapCenter}
          zoom={10}
          options={mapOptions}
        >
          {markers.map((marker) => (
            <Marker
              key={marker.musicEventId.toLocaleString()}
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
