export function GetCurrentUsersPosition(): Promise<google.maps.LatLng | undefined> {
  return new Promise((resolve, reject) => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const userPosition = new google.maps.LatLng(
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
      resolve(new google.maps.LatLng(32.7765, 79.9311));
    }
  });
}
