   export interface EventInfo {
    name: string;
    identifier: string;
    url: string;
    image: string;
    datePublished: string;
    dateModified: string;
    eventStatus: string;
    startDate: string;
    endDate: string;
    previousStartDate: string;
    doorTime: string;
    location: Location;
    offers: Offer[];
    performer: Performer[];
    eventAttendanceMode: string;
    isAccessibleForFree: boolean;
    promoImage: string;
    eventType: string;
    streamIds: string[];
    headlinerInSupport: boolean;
    customTitle: string;
    subtitle: string;
  }
  
   export interface Location {
    name: string;
    identifier: string;
    url: string;
    image: string;
    sameAs: UrlType[];
    datePublished: string;
    dateModified: string;
    maximumAttendeeCapacity: number;
    address: Address;
    geo: GeoCoordinates;
    events: EventInfo[];
    isPermanentlyClosed: boolean;
    numUpcomingEvents: number;
  }
  
  export interface Address {
    type: string;
    streetAddress: string;
    addressLocality: string;
    postalCode: string;
    addressRegion: AddressRegion;
    addressCountry: AddressCountry;
    streetAddress2: string;
    timezone: string;
    jamBaseMetroId: number;
    jamBaseCityId: number;
  }
  
  export interface AddressRegion {
    type: string;
    identifier: string;
    name: string;
    alternateName: string;
  }
  
  export interface AddressCountry {
    type: string;
    identifier: string;
    name: string;
    alternateName: string;
  }
  
   export interface GeoCoordinates {
    musicEventId: number;
    latitude: number;
    longitude: number;
  }
  
  export interface Offer {
    name: string;
    identifier: string;
    url: string;
    image: string;
    sameAs: UrlType[];
    datePublished: string;
    dateModified: string;
    category: string;
    priceSpecification: PriceSpecification;
    seller: Seller;
    validFrom: string;
  }
  
  export interface PriceSpecification {
    type: string;
    minPrice: number;
    maxPrice: number;
    price: number;
    priceCurrency: string;
  }
  
  export interface Seller {
    type: string;
    identifier: string;
    disambiguatingDescription: string;
    name: string;
    url: string;
    image: string;
    sameAs: UrlType[];
    datePublished: string;
    dateModified: string;
  }
  
  export interface Performer {
    name: string;
    identifier: string;
    url: string;
    image: string;
    sameAs: UrlType[];
    datePublished: string;
    dateModified: string;
    type: string;
    member: Performer[];
    memberOf: Performer[];
    foundingLocation: Place;
    foundingDate: string;
    genre: string[];
    events: EventInfo[];
    bandOrMusician: string;
    numUpcomingEvents: number;
    performanceDate: string;
    performanceRank: number;
    isHeadliner: boolean;
    dateIsConfirmed: boolean;
  }
  
  export interface Place {
    type: string;
    name: string;
  }
  
  
  export interface UrlType {
    type: string;
    identifier: string;
    url: string;
  }
  
  export interface MarkerInformation {
    musicEventId: number | number[];
    location: GeoCoordinates;
  }

  export interface Filter {
    startDate: Date;
    endDate: Date;
  }