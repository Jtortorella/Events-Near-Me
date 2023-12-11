export function GetCurrentUsersPosition(): Promise<GeolocationCoordinates> {
    return new Promise((resolve, reject) => {
      if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            resolve(position.coords);
          },
          (error) => {
            console.error("Error happened:", error);
            reject("Error getting location");
          }
        );
      } else {
        /* geolocation IS NOT available */
        console.error("Geolocation is not supported by your browser.");
        reject("Geolocation is not supported");
      }
    });
  }
  