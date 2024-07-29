import "leaflet/dist/leaflet.css"; // Import Leaflet CSS

import { MapContainer } from "react-leaflet/MapContainer";
import "./MapStyles.css";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { useContext, useEffect, useState } from "react";
import { LatLng } from "leaflet";
import MapComponent from "./MapComponent";
import InfoWindowContent from "../InfoWindowComponent/InfoWindowContent";
import { ConcertDataContext } from "../../Context/Context";
import React from "react";
export default function LeafletMapComponent() {

  const { setIsLoading, selectedEventId, isLoading, isError, setMapCenter, mapCenter }: any =
    useContext(ConcertDataContext);

  useEffect(() => {
    setIsLoading(true);
    handleDefaultCenter();
    setIsLoading(false);
  }, []);

  const setClasses = () => {
    let styles = "";
    if (selectedEventId != -1) {
      styles += "grid";
    }
    if (isLoading == true || isError == true) {
      styles += " blur";
    }
    return styles;
  };

  async function handleDefaultCenter(): Promise<LatLng | undefined> {
    try {
      setMapCenter(await GetCurrentUsersPosition());
    } catch (error) {
      console.error("Error setting default centers", error);
      return undefined;
    }
  }

  return (
    <div id="container" className={setClasses()}>
      <InfoWindowContent />
      <MapContainer
        style={{ height: "65vh", width: "80vw", margin: "0 auto" }}
        center={mapCenter}
        zoom={10}
        scrollWheelZoom={false}
      >
        <MapComponent />
      </MapContainer>
    </div>
  );
}
