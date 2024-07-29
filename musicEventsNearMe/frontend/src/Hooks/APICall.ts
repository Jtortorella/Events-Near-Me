import { convertEndDateTime, convertStartDateTime } from "./HandleDateTime";
import { get } from "../Services/APIService";
import { EventInfo, Filter, KeyWordResult, MapBounds } from "../Interfaces/AppInterfaces";
import { LatLngBounds } from "leaflet";

export async function getEventMarkersWithFilter(
  bounds: LatLngBounds | undefined,
  filter: Filter,
  setIsLoading: any,
  setIsError : any,
  setErrorMessage: any,
) {
  if (bounds) {
    setIsLoading(true);
    let mapBounds: MapBounds | undefined =
      getMapBoundsObjectFromCurrentMapLatLngBounds(bounds, filter);
    try {
        return await get({
          url: `http://localhost:8080/api/events?latitudeHigh=${encodeURIComponent(
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
    } catch (error) {
      setIsError(true);
      setErrorMessage("Unable to connect to the database at this time");
    }
    setIsLoading(false);
  }
}

export async function getEventMarkersWithKeyWordAndMapBounds(
  setIsLoading: any,
  setIsError: any,
  keyWord: KeyWordResult,
  setErrorMessage: any,
  bounds: LatLngBounds | undefined,
  filter: Filter
) {
  if (bounds) {
    setIsLoading(true);
    try {
      let mapBounds: MapBounds | undefined =
        getMapBoundsObjectFromCurrentMapLatLngBounds(bounds, filter);
        return await get({
          url: `http://localhost:8080/api/events/search?` +
               `value=${encodeURIComponent(keyWord.value)}` +
               `&keyWordType=${encodeURIComponent(keyWord.keyWordType)}` +
               `&latitudeHigh=${encodeURIComponent(mapBounds!.latitudeHigh)}` +
               `&latitudeLow=${encodeURIComponent(mapBounds!.latitudeLow)}` +
               `&longitudeHigh=${encodeURIComponent(mapBounds!.longitudeHigh)}` +
               `&longitudeLow=${encodeURIComponent(mapBounds!.longitudeLow)}` +
               `&startDate=${encodeURI(mapBounds!.startDate.toString())}` +
               `&endDate=${encodeURI(mapBounds!.endDate.toString())}`
        });
        
    } catch (error) {
      setIsError(true);
      setErrorMessage("Unable to connect to the database at this time");
    }
    setIsLoading(false);
  }
}

export async function getEventMarkersWithKeyWord(
  setIsLoading: any,
  setIsError : any,
  keyWord: KeyWordResult,
  setErrorMessage: any,
) {
  setIsLoading(true);
  try {
    return await get({
      url: `http://localhost:8080/api/events/search?` +
      `value=${encodeURIComponent(keyWord.value)}` +
      `&keyWordType=${encodeURIComponent(keyWord.keyWordType)}`
    })
} catch (error) {
setIsError(true);
setErrorMessage("Unable to connect to the database at this time");
}
setIsLoading(false);
}


export async function getEventMarkersWithKeyWordAndFilter(
  bounds: LatLngBounds | undefined,
  filter: Filter,
  setIsLoading: any,
  setIsError : any,
  keyWord: KeyWordResult,
  setErrorMessage: any,
) {
  if (bounds) {
    setIsLoading(true);
    let mapBounds: MapBounds | undefined =
      getMapBoundsObjectFromCurrentMapLatLngBounds(bounds, filter);
    try {
          return await get({
            url: `http://localhost:8080/api/filtered-events/search?` +
            `value=${encodeURIComponent(keyWord.value)}` +
            `&keyWordType=${encodeURIComponent(keyWord.keyWordType)}` +
            `&latitudeHigh=${encodeURIComponent(mapBounds?.latitudeHigh || '')}` +
            `&latitudeLow=${encodeURIComponent(mapBounds?.latitudeLow || '')}` +
            `&longitudeLow=${encodeURIComponent(mapBounds?.longitudeLow || '')}` +
            `&longitudeHigh=${encodeURIComponent(mapBounds?.longitudeHigh || '')}` +
            `&startDate=${encodeURIComponent(mapBounds?.startDate?.toString() || '')}` +
            `&endDate=${encodeURIComponent(mapBounds?.endDate?.toString() || '')}`,
          })
    } catch (error) {
      setIsError(true);
      setErrorMessage("Unable to connect to the database at this time");
    }
    setIsLoading(false);
  }
}



export function getMapBoundsObjectFromCurrentMapLatLngBounds(
  bounds: LatLngBounds,
  filter: Filter
) {
  if (bounds) {
    const northeast = bounds.getNorthEast();
    const southwest = bounds.getSouthWest();
    return {
      latitudeHigh: northeast.lat,
      latitudeLow: southwest.lat,
      longitudeHigh: northeast.lng,
      longitudeLow: southwest.lng,
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
  const url = `http://localhost:8080/api/events/`;
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
