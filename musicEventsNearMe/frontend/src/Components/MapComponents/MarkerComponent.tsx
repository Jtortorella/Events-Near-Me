import { useContext, useEffect, useState } from "react";
import {
  GeoCoordinates,
  MarkerInformation,
} from "../../Interfaces/AppInterfaces";
import { Marker } from "react-leaflet";
import { getEventDetails } from "../../Hooks/APICall";
import { ConcertDataContext } from "../../Context/Context";
import {Icon} from 'leaflet';
import React from "react";

export const MarkerComponent = () => {
  const { locations, setIsLoading,  setSelectedEventId }: any =
    useContext(ConcertDataContext);
  const [markers, setMarkers] = useState<MarkerInformation[]>([]);

  useEffect(() => {
    createMarkers();
    setIsLoading(false);
  }, []);

  useEffect(() => {
    createMarkers();
    setIsLoading(false);
  }, [locations]);

  const createMarkers = () => {
    let currentGeoLocationOfEvents: GeoCoordinates[] = [];
    let currentMarkers: MarkerInformation[] = [];
    if (Array.isArray(locations)) {
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
    }
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

  function handleMarkerClick(musicEventId: number | number[]) {
      setSelectedEventId(musicEventId);
  
  }

  return (
    <>
      {markers.map((marker: MarkerInformation) => (
        <Marker
          key={marker.musicEventId.toLocaleString()}
          position={{
            lat: marker.location.latitude,
            lng: marker.location.longitude,
          }}
          eventHandlers={{
            click: () => {
              handleMarkerClick(marker.musicEventId);
            },
          }}
        ></Marker>
      ))}
    </>
  );
};
