import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import { TileLayer, useMapEvents } from "react-leaflet";
import "./MapStyles.css";
import { useContext, useEffect } from "react";
import { Context } from "../../Context/Context";
import { getEventMarkers } from "../../Hooks/APICall";
import { LatLngBounds } from "leaflet";
import { MarkerComponent } from "./MarkerComponent";

function MapComponent() {
  const { handleGeoCoordinatesImport, setIsLoading, filter, setIsError}: any =
    useContext(Context);

    useEffect(() => {
      handleMapBoundChangeOrFilterChange(map.getBounds());
    }, []);

    useEffect(() => {
      handleMapBoundChangeOrFilterChange(map.getBounds());
    }, [filter]);

  const map = useMapEvents({
    moveend() {
      handleMapBoundChangeOrFilterChange(map.getBounds());
    },
  });

  const handleMapBoundChangeOrFilterChange = async (
    bounds: LatLngBounds
  ): Promise<void> =>
    getEventMarkers(bounds, filter, setIsLoading, setIsError, handleGeoCoordinatesImport);

  return (
    <div>
      <TileLayer url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"} />
      <MarkerComponent></MarkerComponent>
    </div>
  );
}

export default MapComponent;
