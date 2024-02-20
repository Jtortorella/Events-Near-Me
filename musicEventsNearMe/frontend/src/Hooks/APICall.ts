import { convertEndDateTime, convertStartDateTime } from "./HandleDateTime";
import { get } from "../Services/APIService";
import { EventInfo, Filter, MapBounds } from "../Interfaces/AppInterfaces";

export async function getEventMarkers(
  bounds: google.maps.LatLngBounds | undefined,
  filter: Filter,
  setIsLoading: any,
  handleGeoCoordinatesImport: any
) {
  if (bounds) {
    setIsLoading(true);
    let mapBounds: MapBounds | undefined =
      getMapBoundsObjectFromCurrentMapLatLngBounds(bounds, filter);
    try {
      handleGeoCoordinatesImport(
        await get({
          url: `http://localhost:8080/concertData/events?latitudeHigh=${encodeURIComponent(
            mapBounds!.latitudeHigh
          )}&latitudeLow=${encodeURIComponent(
            mapBounds!.latitudeLow
          )}&longitudeLow=${encodeURIComponent(
            mapBounds!.longitudeHigh
          )}&longitudeHigh=${encodeURIComponent(
            mapBounds!.longitudeLow
          )}&startDate=${encodeURI(
            mapBounds!.startDate.toString()
          )}&endDate=${encodeURI(mapBounds!.endDate.toString())}`,
        })
      );
    } catch (error) {
      console.error(error);
      setIsLoading(false);
    }
  }
}
function getMapBoundsObjectFromCurrentMapLatLngBounds(
  bounds: google.maps.LatLngBounds,
  filter: Filter
) {
  if (bounds) {
    const northeast = bounds.getNorthEast();
    const southwest = bounds.getSouthWest();
    return {
      latitudeHigh: northeast.lat(),
      latitudeLow: southwest.lat(),
      longitudeHigh: northeast.lng(),
      longitudeLow: southwest.lng(),
      startDate: filter
        ? convertStartDateTime(filter.startDate)
        : convertStartDateTime(new Date()),
      endDate: filter
        ? convertEndDateTime(filter.endDate)
        : convertEndDateTime(new Date()),
    };
  }
}

export async function getEventDetails(
  id: number | number[]
): Promise<EventInfo[] | undefined> {
  const url = `http://localhost:8080/concertData/event/`;
  if (Array.isArray(id)) {
    try {
      const responses = await Promise.all(
        id.map(async (eventId) => {
          try {
            return await get({ url: url.concat(eventId.toString()) });
          } catch (err) {
            console.error("Error fetching event details:", err);
            return null;
          }
        })
      );
      const validResponses = responses.filter((response) => response !== null);
      return validResponses.length > 0 ? validResponses : undefined;
    } catch (err) {
      console.error("Error fetching event details:", err);
      return undefined;
    }
  } else if (Number.isInteger(id)) {
    try {
      const response = await get({ url: url.concat(id.toString()) });
      return Array.isArray(response) ? response : [response];
    } catch (err) {
      console.error("Error fetching event details:", err);
      return undefined;
    }
  }
  return undefined;
}
