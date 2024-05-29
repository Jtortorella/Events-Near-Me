
export interface Address {
  addressType: any;
  streetAddress: string;
  postalCode: string;
  addressLocality: string;
  streetAddress2: string | null;
  addressRegion: AddressRegion;
  addressCountry: AddressCountry;
}

export interface AddressCountry {
  name: string;
}

export interface AddressRegion {
  name: string;
}

export interface EventInfo {
  name: string;
  url: string;
  image: string;
  eventStatus: string;
  startDate: string;
  endDate: string;
  doorTime: string | null;
  eventAttendanceMode: string;
  promoImage: any;
  eventType: any;
  location: Location;
  offers: Offer[];
  performer: Performer[];
  accessibleForFree: boolean;
}

export interface Filter {
  startDate: Date;
  endDate: Date;
  reset: boolean;
}

export interface GeoCoordinates {
  musicEventId: number;
  latitude: number;
  longitude: number;
}

export interface LatLng {
  latitude: number;
  longitude: number;
}

export interface Genre {
  genreName: string;
}

export interface Location {
  name: string;
  url: string;
  image: string;
  maximumAttendeeCapacity: number;
  numUpcomingEvents: number;
  address: Address;
  geo: LatLng;
  permanentlyClosed: boolean;
}
export interface MapBounds {
  latitudeHigh: number;
  latitudeLow: number;
  longitudeHigh: number;
  longitudeLow: number;
  startDate: String;
  endDate: String;
}
export interface MarkerInformation {
  musicEventId: number | number[];
  location: GeoCoordinates;
}


export interface Offer {
  name: string;
  url: string;
  category: string;
  validFrom: string;
  priceSpecification: PriceSpecification;
  seller: Seller;
}

export interface Performer {
  name: string;
  url: string;
  image: string;
  genres: Genre[];
}

export interface PriceSpecification {
  price: string;
  priceCurrency: string;
}

export interface Seller {
  name: string;
}

