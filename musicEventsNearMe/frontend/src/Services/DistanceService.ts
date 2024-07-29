import { GeoCoordinates } from "../Interfaces/AppInterfaces";

export function sortByDistanceFromCenter(res: GeoCoordinates[], mapCenter: any): GeoCoordinates[] {
    if (res && mapCenter) {
        return res.sort((a, b) => determineByDistance(mapCenter, a, b));
    }
    else {
        return res;
    }
}

function calcCrowDistanceFromCenter(mapCenter: any, otherCoordinate: GeoCoordinates): number {
    const R = 6371; // Earth's radius in km
    const dLat = toRad(otherCoordinate.latitude - mapCenter?.lat);
    const dLon = toRad(otherCoordinate.longitude - mapCenter?.lng);
    const lat1 = toRad(mapCenter.lat);
    const lat2 = toRad(otherCoordinate.latitude);

    const a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
              Math.sin(dLon/2) * Math.sin(dLon/2) * 
              Math.cos(lat1) * Math.cos(lat2); 
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    const d = R * c;
    return d;
}

function toRad(value: number): number {
    return value * Math.PI / 180;
}

function determineByDistance(mapCenter: any, firstCoordinate: GeoCoordinates, secondCoordinate: GeoCoordinates): number {
    return calcCrowDistanceFromCenter(mapCenter, firstCoordinate) - calcCrowDistanceFromCenter(mapCenter, secondCoordinate);
}