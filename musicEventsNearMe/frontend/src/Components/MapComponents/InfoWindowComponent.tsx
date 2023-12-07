import { Event, Performer } from "../../Interfaces/EventApiResponse";
import {convertLocalDateTimeToDateTime} from "../../Hooks/HandleDateTime"
export const InfoWindow = (event: Event) => {

  let name = event.name;
  let startDateTime = convertLocalDateTimeToDateTime(event.startDate);
  let endDateTime = event.endDate;
  if (endDateTime.includes('T')) {
    convertLocalDateTimeToDateTime(endDateTime);
  }
  else {
    endDateTime = '';
  }
  let performers: Performer[] = event.performer;
  let venue = event.location.name;

  const performerStr = getAllPerformers(performers);
  return `<div className="infoWindowContainer">
            <p className="infoWindowText">
                ${name}
                <br></br>
                ${startDateTime}
                <br></br>
                ${performerStr}
                ${venue}
            </p>
        </div>`;
};

function getAllPerformers(performers: Performer[]) {
  let performerStr = ``;
  for (let performer of performers) {
    performerStr += performer.name + "<br></br>";
  }
  return performerStr;
}
