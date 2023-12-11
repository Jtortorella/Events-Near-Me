import { EventInfo } from "../../Interfaces/EventApiResponse";

export class VenueDataStore {
  allVenueEvents: EventInfo[] | EventInfo;
  multipleEvents: boolean;
  onLastPage: boolean;
  onFirstPage: boolean;
  lastPage: number;
  pageIndex: number;

  constructor(event: EventInfo[] | EventInfo) {
    this.allVenueEvents = event;
    this.pageIndex = 0;
    this.onFirstPage = true;
    if (Array.isArray(event)) {
      this.multipleEvents = true;
      this.lastPage = event.length;
      this.onLastPage = false;
    } else {
      this.multipleEvents = false;
      this.onLastPage = true;
      this.lastPage = 0;
    }
  }

  getEventInformationAtCurrentIndex = () => {
    if (Array.isArray(this.allVenueEvents)) {
      return this.allVenueEvents[this.pageIndex];
    } else {
      return this.allVenueEvents;
    }
  };

  incrementIndex = () => {
    if (this.pageIndex == this.lastPage - 1) {
      this.onLastPage = true;
    } else {
      this.pageIndex++;
    }
    if (this.pageIndex != 0) {
      this.onFirstPage = false;
    }
  };
  
  decrementIndex = () => {
    if (this.pageIndex == 0) {
      this.pageIndex = 0;
      this.onFirstPage = true;
    } else {
      this.pageIndex--;
    }
    if (this.lastPage != this.pageIndex) {
      this.onLastPage = false;
    }
  };
  getMultipleEvents = () => {
    return this.multipleEvents;
  };
  getOnLastPage = () => {
    return this.onLastPage;
  };
  getOnFirstPage = () => {
    return this.onFirstPage;
  };
}
