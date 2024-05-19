import { LatLng } from "leaflet";

export async function GetCurrentUsersPosition(): Promise<LatLng | undefined> {
  return new Promise((resolve, reject) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const userPosition: LatLng = new LatLng(
            position.coords.latitude,
            position.coords.longitude
          );
          resolve(userPosition);
        },
        (error) => {
          console.error("Error getting user position:", error);
          reject(undefined);
        }
      );
    } else {
      console.log("Error getting user position, returning default position.");
      resolve(new LatLng(32.7765, 79.9311));
    }
  });
}
