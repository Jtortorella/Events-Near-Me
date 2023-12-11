import { EventInfo } from "../../Interfaces/EventApiResponse";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";

const InfoWindow = (
  eventInfo: EventInfo,
  onLastPage?: boolean,
  onFirstPage?: boolean
) => {
  const { name, startDate, endDate, performer, location } = eventInfo;

  const performerStr = getAllPerformers(performer);
  

  return (
    `<div class="infoWindowContainer">
      <p class="infoWindowText">
        ${name}<br />
        ${convertLocalDateTimeToDateTime(startDate)} - ${convertLocalDateTimeToDateTime(endDate)}<br />
        ${performerStr}<br />
        ${location.name}<br />
        ${generateSuffix(onLastPage, onFirstPage)}
        <div id="button-container"></div>
      </p>
    </div>`
  );
};

function getAllPerformers(performers: Array<any> | undefined) {
  if (!performers) return "";
  return performers.map((performer) => performer.name + "<br />").join("");
}

function generateSuffix(onLastPage: boolean | undefined, onFirstPage: boolean | undefined) {

  let suffix = ``;
    if (!onFirstPage) {
      suffix += `
        <button
          class="prev-button button"
          id="info-prev-button"
        >
          Previous
        </button> 
      `;
    }
    if (!onLastPage) {
      suffix += `
        <button 
          class="next-button button"
          id="info-next-button"
        >
          Next
        </button>
      `;
    
  }
  return suffix;
}

export default InfoWindow;
