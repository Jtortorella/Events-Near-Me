import React, { useContext, useEffect, useRef, useState } from "react";
import { GoogleMap, InfoWindow } from "@react-google-maps/api";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import { MarkerInformation } from "../../Interfaces/AppInterfaces";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { mapOptions } from "../../Styles/MapOptions";
import { mapContainerStyles } from "../../Styles/MapStyles";
import InfoWindowContent from "./InfoWindowContent";
import { getEventMarkers } from "../../Hooks/APICall";
import { MarkerComponent } from "./MarkerComponent";

const MapComponent: React.FC = () => {
  const { handleGeoCoordinatesImport, setIsLoading, filter }: any =
    useContext(Context);

  const mapRef = useRef<google.maps.Map | undefined>(undefined);

  const [selectedMarker, setSelectedMarker] = useState<
    MarkerInformation | undefined
  >(undefined);

    const [infoWindowDisplaying, setInfoWindowDisplaying] =
    useState<boolean>(false);

  const [mapCenter, setMapCenter] = useState<google.maps.LatLng | undefined>(
    undefined
  );
  


  useEffect(() => {
    setIsLoading(true);
    handleDefaultCenter();
  }, []);

  useEffect(() => {
    setIsLoading(true);
    handleMapBoundChangeOrFilterChange();
    setInfoWindowDisplaying(false);
    setIsLoading(false);
  }, [filter]);

  const setMapReferenceToCurrentMap = (event: google.maps.Map) => {
    mapRef.current = event;
  };

  const handleMapBoundChangeOrFilterChange = async (): Promise<void> => {
    getEventMarkers(
      mapRef.current?.getBounds(),
      filter,
      setIsLoading,
      handleGeoCoordinatesImport
    );
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

  const handleMarkerClick = (marker: MarkerInformation) => {
    setInfoWindowDisplaying(true);
    setSelectedMarker(marker);
  };

  return (
    <>
      <div id="mapContainer" className="mapContainer">
        <GoogleMap
          onLoad={(map: google.maps.Map) => setMapReferenceToCurrentMap(map)}
          onBoundsChanged={handleMapBoundChangeOrFilterChange}
          mapContainerStyle={mapContainerStyles}
          center={mapCenter}
          zoom={5}
          options={mapOptions}
        >
          <MarkerComponent
            handleMarkerClick={handleMarkerClick}
          ></MarkerComponent>
          {infoWindowDisplaying &&
            selectedMarker &&
            <InfoWindow
            position={{
              lat: selectedMarker.location.latitude,
              lng: selectedMarker.location.longitude,
            }}
            onCloseClick={() => {
              setInfoWindowDisplaying(false);
              setSelectedMarker(undefined);
            }}
          >
            <InfoWindowContent musicEventId={selectedMarker.musicEventId} />
          </InfoWindow>}
        </GoogleMap>
      </div>
    </>
  );
};

export default MapComponent;
