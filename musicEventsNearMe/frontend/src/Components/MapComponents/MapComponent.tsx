import { useContext, useEffect, useRef, useState } from "react";
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

const MapComponent = () => {
  const { handleGeoCoordinatesImport, setIsLoading, locations, filter }: any = useContext(Context);
  const mapRef = useRef<google.maps.Map | undefined>(undefined);
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
    handleMapBoundChangeOrFilterChange();
  }, [filter, filteredMarkers]);

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
    const index = currentMarkers.findIndex((marker) => marker.location === location);
    if (index !== -1) {
      let previousMusicEventId: number | number[] = currentMarkers[index].musicEventId;
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
    //See below, probably in our best interest to not recreate everytime.
    //Context is also just reinstantiating the whole thing.
    let currentGeoLocationOfEvents: GeoCoordinates[] = [];
    let currentMarkers: MarkerInformation[] = [];
    for (const location of locations || []) {
      const index = checkIfLocationAlreadyExists(
        location,
        currentGeoLocationOfEvents
      );
      if (index === -1) { //If event does not already exist.
        currentGeoLocationOfEvents = handleSetGeoLocation(
          location,
          currentGeoLocationOfEvents
        );
        currentMarkers = handleAddNewMarker(location, currentMarkers);
      }
      else {
        currentMarkers = handleUpdateMarker(location, currentMarkers);
      }
    }
    setMarkers(currentMarkers);
    setGeoLocationOfEvents(currentGeoLocationOfEvents);
  };

 

  const handleMapBoundChangeOrFilterChange = async () => {
    setIsLoading(true);
  const currentDate = new Date();
    currentDate.setHours(currentDate.getHours() - 5); //Need to check why computer is always 5 hours off.
    const bounds = mapRef.current?.getBounds();
    if (bounds) {
      const northeast = bounds.getNorthEast();
      const southwest = bounds.getSouthWest();
      const mapBounds: MapBounds = {
        latitudeHigh: northeast.lat(),
        latitudeLow: southwest.lat(),
        longitudeHigh: northeast.lng(),
        longitudeLow: southwest.lng(),
        startDate: filter?.startDate.toISOString().slice(0, 10) + 'T00:00:00' || new Date().toISOString().slice(0, 10) + 'T00:00:00',
        endDate: filter?.endDate.toISOString().slice(0, 10) + 'T23:59:59' || new Date().toISOString().slice(0, 10) + 'T23:59:59',
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
        <InfoWindowContent eventInfo={getEventDetails()} />
      </InfoWindow>
    );
  };
  function getEventDetails(): EventInfo[] | undefined {
    return undefined;
  }

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
