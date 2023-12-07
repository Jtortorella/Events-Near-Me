import { useContext, useEffect, useRef, useState } from "react";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import { getCurrentUsersPosition } from "./getCurrentUsersPosition";
import { InfoWindow } from "./InfoWindowComponent";
import { Event } from "../../Interfaces/EventApiResponse";
import { mapStyles } from "../../Styles/MapStyles";
import microphone from "../../assets/Microphone.svg";

interface MarkerInformation {
  marker: google.maps.Marker;
  location?: any;
  event: Event;
}

const MapComponent = () => {
  const apiKey = "AIzaSyCww45TrPXdk_vRa4sKRU-8Gzcd9jr6J2M";
  const { isLoading, events }: any = useContext(Context);
  const [markersInformation, setMarkersInformation] = useState<
    MarkerInformation[]
  >([]);
  const [markerLocation, setMarkerLocation] = useState<
    GeolocationCoordinates[]
  >([]);

  const previousWindowRef = useRef<google.maps.InfoWindow | null>(null);
  let map: google.maps.Map;
  let infowindow: google.maps.InfoWindow | null = null;

  const createMarkers = () => {
    for (let event of events) {
      let location: GeolocationCoordinates = event.location.geo;
      if (markerLocation.includes(location)) {
        for (let index = 0; index < markersInformation.length; index++) {
          if (
            markersInformation[index].location.latitude === location.latitude &&
            markersInformation[index].location.longitude === location.longitude
          ) {
            if (Array.isArray(markersInformation[index].event)) {
              setMarkersInformation((prev: any) => prev[index].event.concat(event));
            }
            else {
              setMarkersInformation((prev:any) => [...prev[index].event, event]);
            }
          }
        }
      } else {
        setMarkerLocation((prev: any) => [...prev, location]);
      }

        const marker = new google.maps.Marker({
          position: new google.maps.LatLng(
            location.latitude,
            location.longitude
          ),
          map,
          icon: microphone,
        });

        let markerInformation: MarkerInformation = {
          marker: marker,
          location: location,
          event: event,
        };
        setMarkersInformation((prev: any) => [...prev, markerInformation]);
    } 

    
  };

  const attachMarkerClickEvent = (
    marker: google.maps.Marker,
    location: any,
    event: Event,
  ) => {
    google.maps.event.addListener(marker, "click", () => {
      if (previousWindowRef.current) {
        previousWindowRef.current.close();
      }
      infowindow = new google.maps.InfoWindow({
        content: InfoWindow(event),
      });
      infowindow.open(map, marker);
      previousWindowRef.current = infowindow;
    });
  };

  const initMap = async () => {
    let latitude;
    let longitude;

    try {
      ({ latitude, longitude } = await getCurrentUsersPosition());
    } catch (error) {
      console.error("Error occurred:", error);
      latitude = 40.7128;
      longitude = -74.006;
    }

    map = new window.google.maps.Map(
      document.getElementById("map") as HTMLElement,
      {
        center: { lat: latitude, lng: longitude },
        zoom: 10,
        gestureHandling: "auto",
        zoomControl: false,
        styles: mapStyles,
        streetViewControl: false,
        mapTypeControl: false,
        fullscreenControl: false,
      }
    );
  };

  useEffect(() => {
    if (!isLoading) {
      const script = document.createElement("script");
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
      script.defer = true;
      script.async = true;

      script.onload = async () => {
        await initMap();
        await createMarkers();
      };
      console.log(markersInformation);

      document.head.appendChild(script);

      return () => {
        document.head.removeChild(script);

        // Close the info window when the component unmounts
        if (infowindow) {
          infowindow.close();
        }
      };
    }
  }, [isLoading]);

  return (
    <div id="mapContainer" className="mapContainer">
      <div id="map" className="map"></div>
    </div>
  );
};

export default MapComponent;
