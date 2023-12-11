import { useContext, useEffect, useRef } from "react";
import "../../Styles/MapCompentStyles.css";
import { Context } from "../../Context/Context";
import { GetCurrentUsersPosition } from "../../Services/GetCurrentUsersPosition";
import { EventInfo } from "../../Interfaces/EventApiResponse";
import { mapStyles } from "../../Styles/MapStyles";
import microphone from "../../assets/Microphone.svg";
import InfoWindow from "./InfoWindowComponent";
import { VenueDataStore } from "./VenueDataStore";

interface MarkerInformation {
  marker: google.maps.Marker;
  event: EventInfo | EventInfo[];
  location: GeolocationCoordinates;
}

const MapComponent = () => {
  const apiKey = "AIzaSyCww45TrPXdk_vRa4sKRU-8Gzcd9jr6J2M";
  const { isLoading, events }: any = useContext(Context);

  const previousWindowRef = useRef<google.maps.InfoWindow | null>(null);
  let map: google.maps.Map;
  let infowindow: google.maps.InfoWindow | null = null;

  const createMarkers = () => {
    let previouslyEstablishedData: MarkerInformation[] = [];
    for (let event of events) {
      const location = event.location.geo;
      let locationExists: number = -1;
      if (previouslyEstablishedData.length > 0) {
        locationExists = previouslyEstablishedData.findIndex(
          (prevLocation) =>
            prevLocation.location.latitude === location.latitude &&
            prevLocation.location.longitude === location.longitude
        );
      }
      if (locationExists === -1) {

        previouslyEstablishedData.push({
          location: location,
          marker: new google.maps.Marker({
            position: new google.maps.LatLng(
              location.latitude,
              location.longitude
            ),
            map: map, // Assuming `map` is defined somewhere in your code
          }),
          event: [event], // Wrap event in an array
        });
      } else {
        const existingEvent = previouslyEstablishedData[locationExists]
          .event as EventInfo[];
        if (Array.isArray(existingEvent)) {
          existingEvent.push(event);
        } else {
          previouslyEstablishedData[locationExists].event = [
            previouslyEstablishedData[locationExists].event,
            event,
          ];
        }
      }
      for (let markers of previouslyEstablishedData) {
        attachMarkerClickEvent(markers);
      }
    }
  };

  const attachMarkerClickEvent = (marker: MarkerInformation) => {
    google.maps.event.addListener(marker.marker, "click", () => {
      let venueDataStore: VenueDataStore = new VenueDataStore(marker.event);
      let multipleEvents = venueDataStore.getMultipleEvents();
      if (previousWindowRef.current) {
        previousWindowRef.current.close();
      }

      if (venueDataStore.getMultipleEvents()) {
        addInfoWindowListeners(marker, venueDataStore, multipleEvents);
      }
      else {
        initInfoWindow(marker, venueDataStore, multipleEvents);
      }
      previousWindowRef.current = infowindow;
    });
  };

const initInfoWindow = (marker: MarkerInformation, venueDataStore: VenueDataStore, multipleEvents: boolean) => {
    infowindow = new google.maps.InfoWindow({
    content: InfoWindow(venueDataStore.getEventInformationAtCurrentIndex(), venueDataStore.getOnLastPage(), venueDataStore.getOnFirstPage()),
  });
  infowindow.open(map, marker.marker);
  return infowindow;
}

function addInfoWindowListeners(marker: MarkerInformation, venueDataStore: VenueDataStore, multipleEvents: boolean) {
  const infoWindow = initInfoWindow(marker, venueDataStore, multipleEvents);

  const handlePrevButtonClick = () => {
    infoWindow.close();
    venueDataStore.decrementIndex();
    addInfoWindowListeners(marker, venueDataStore, multipleEvents);
    previousWindowRef.current = infoWindow;
  };

  const handleNextButtonClick = () => {
    infoWindow.close();
    venueDataStore.incrementIndex();
    addInfoWindowListeners(marker, venueDataStore, multipleEvents);
    previousWindowRef.current = infoWindow;
  };

  const handleDomReady = () => {
    document.getElementById("info-prev-button")?.removeEventListener("click", handlePrevButtonClick);
    document.getElementById("info-next-button")?.removeEventListener("click", handleNextButtonClick);
    document.getElementById("info-prev-button")?.addEventListener("click", handlePrevButtonClick);
    document.getElementById("info-next-button")?.addEventListener("click", handleNextButtonClick);
  };

  // Remove existing "domready" event listener before adding a new one
  google.maps.event.clearListeners(infoWindow, "domready");
  google.maps.event.addListener(infoWindow, "domready", handleDomReady);
}


  const initMap = async () => {
    let latitude;
    let longitude;

    try {
      ({ latitude, longitude } = await GetCurrentUsersPosition());
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
        keyboardShortcuts: false,
        disableDefaultUI: true,
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
