export interface EventInfo {
  name: string;
  url: string;
  location_name: string;
  location_url: string;
  performers_details: string;
  start_date: string;
  image: string;
  street_address: string;
  end_date: string;
  promo_image: string | null;
  door_time: string;
  postal_code: string;
  address_locality: string;
  event_status: string;
  is_accessible_for_free: boolean;
  location_id: string;
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
    id: string;
    name: string;
    url: string;
    numUpcomingEvents: number;
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