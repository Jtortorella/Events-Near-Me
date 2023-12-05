import React, { useEffect } from "react";
import "./MapCompentStyles.css";

interface MapComponentProps {
  eventData: any[];
  apiKey: string;
}

const MapComponent: React.FC<MapComponentProps> = ({ eventData, apiKey }) => {
  let map: google.maps.Map;
  let infowindow: google.maps.InfoWindow;

  const createMarkers = () => {
    for (let event of eventData) {
      const location = event['location']['geo'];
      if (location && location.latitude && location.longitude) {
        const marker = new google.maps.Marker({
          position: new google.maps.LatLng(location.latitude, location.longitude),
          map,
        });
        attachMarkerClickEvent(marker);
      }
    }
  };

  const attachMarkerClickEvent = (marker: google.maps.Marker) => {
    google.maps.event.addListener(marker, 'click', (function(marker) {
      return function() {
        infowindow.setContent("Test!");
        infowindow.open(map, marker);
      }
    })(marker));
  };

  const initMap = () => {
    map = new window.google.maps.Map(document.getElementById("map") as HTMLElement, {
      center: { lat: -34.397, lng: 150.644 },
      zoom: 10,
    });
    createMarkers();
  };

  useEffect(() => {
    // Load the Google Maps script dynamically
    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
    script.defer = true;
    script.async = true;
    script.onload = initMap; // Initialize the map once the script is loaded
    document.head.appendChild(script);

    // Cleanup function to remove the script when the component unmounts
    return () => {
      document.head.removeChild(script);
    };
  }, [apiKey]);

  return (
    <div id="mapContainer" className="mapContainer">
      <div id="map" className="map"></div>
    </div>
  );
};

export default MapComponent;
