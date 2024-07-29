import "leaflet/dist/leaflet.css"; // Import Leaflet CSS
import { TileLayer, useMap, useMapEvents } from "react-leaflet";
import "./MapStyles.css";
import { useContext, useEffect, useMemo } from "react";
import { ConcertDataContext } from "../../Context/Context";
import { LatLngBounds } from "leaflet";
import { MarkerComponent } from "./MarkerComponent";
import React from "react";

function MapComponent() {

  const { filter, setMapBounds, mapCenter, setIsLoading}: any =
    useContext(ConcertDataContext);

    const currentMap = useMap();

    useEffect(() => {
      setIsLoading(true);
      currentMap.flyTo(mapCenter);
      setIsLoading(false);
    }, [mapCenter]);

    //When filter change update map bound or filter.
    useEffect(() => {
      handleMapBoundChangeOrFilterChange(map.getBounds());
    }, [filter]);

        //On first load.
        useEffect(() => {
          handleMapBoundChangeOrFilterChange(map.getBounds());
        }, []);

  const map = useMapEvents({
    moveend() {
      handleMapBoundChangeOrFilterChange(map.getBounds());
    },
  });

  const handleMapBoundChangeOrFilterChange = async (
    bounds: LatLngBounds
  ): Promise<void> => {
    setMapBounds(bounds);
  }

  return (
    <div>
      <TileLayer url={"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"} />
      <MarkerComponent></MarkerComponent>
    </div>
  );
}

export default MapComponent;
