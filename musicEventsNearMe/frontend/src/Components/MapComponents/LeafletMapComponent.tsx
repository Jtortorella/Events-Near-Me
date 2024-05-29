import "leaflet/dist/leaflet.css"; // Import Leaflet CSS

import { MapContainer } from "react-leaflet/MapContainer";
import "./MapStyles.css";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { useContext, useEffect, useState } from "react";
import { Context } from "../../Context/Context";
import { LatLng } from "leaflet";
import MapComponent from "./MapComponent";
import InfoWindowContent from "./InfoWindowContent";
export default function LeafletMapComponent() {
  const [mapCenter, setMapCenter] = useState<LatLng | undefined>(
    new LatLng(32.7765, -79.9311)
  );
  const { setIsLoading, selectedEventId, isLoading, isError}: any = useContext(Context);

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
    return styles
  }

  async function handleDefaultCenter(): Promise<LatLng | undefined> {
    try {
      setMapCenter(await GetCurrentUsersPosition());
    } catch (error) {
      console.error("Error setting default centers", error);
      return undefined;
    }
  }

  return (
<div id="container" className={setClasses()}  >
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
