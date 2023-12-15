import { mapStyles } from "./MapStyles";

export const mapOptions: google.maps.MapOptions = {
    gestureHandling: "auto",
    zoomControl: false,
    streetViewControl: false,
    mapTypeControl: false,
    fullscreenControl: false,
    keyboardShortcuts: false,
    disableDefaultUI: true,
    styles: mapStyles,
  }
