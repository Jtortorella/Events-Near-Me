import { useState } from "react";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";
import { EventInfo } from "../../Interfaces/AppInterfaces";

interface InfoWindowContentProps {
  eventInfo: EventInfo[] | undefined;
}

const InfoWindowContent: React.FC<InfoWindowContentProps> = ({ eventInfo }) => {
  if (!eventInfo || eventInfo.length === 0) {
    return null;
  }
  const [index, setIndex] = useState<number>(0);
  const { name, startDate, performer, location } = eventInfo[index];
  console.log(startDate)

  function handlePreviousButtonClick(): void {
    setIndex((prev) => prev - 1);
  }
  
  function handleNextButtonClick(): void {
    setIndex((prev) => prev + 1);
  }

  return (
    <div className="infoWindowContainer">
      {name}
      <br />
      {convertLocalDateTimeToDateTime(startDate)}
      <br />
      <br />
      PERFORMERS:
      {performer.map((performer, index) => (
        <p>
        {index + 1}: {performer.name} <br />
        </p>
      ))}
      <br />
      {location.name}
      <br />
      <div id="button-container">
      {index !== 0 && (
        <button
          className="button prev-button"
          onClick={handlePreviousButtonClick}
        >PREVIOUS</button>
      )}
      {index !== eventInfo.length - 1 && (
        <button
          className="button next-button"
          onClick={handleNextButtonClick}
        >NEXT</button>
      )}
      </div>
    </div>
  );
};


export default InfoWindowContent;
