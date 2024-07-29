import { useState, useContext } from "react";
import { ConcertDataContext } from "../../Context/Context";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";
import { Performer } from "../../Interfaces/AppInterfaces";
import React from "react";

const InfoWindowContent: React.FC = () => {
  const [index, setIndex] = useState<number>(0);
  const { infoArr }: any = useContext(ConcertDataContext);
  

  if (!infoArr || infoArr.length === 0 || infoArr[index] == undefined) {
    return null;
  }

  const { name, startDate, performer, location } = infoArr[index];

  const handleButtonClick = (direction: number): void => {
    setIndex((prev) => (prev += direction));
  };

  return (
    
    <aside id="infoWindowContainer">
      {name}
      <br />
      {convertLocalDateTimeToDateTime(startDate)}
      <br />
      <br />
      {performer && performer.length > 0 && (
        <>
          PERFORMERS:
          {performer.map((performer: Performer, idx: number) => (
            <p key={idx + 1}>
              {idx + 1}: {performer.name} <br />
            </p>
          ))}
          <br />
        </>
      )}
      <br />
      {location.name}
      <br />
      <div id="button-container">
        <button
          className={"button prev-button" + (index == 0 ? " hidden" : "")}
          onClick={() => handleButtonClick(-1)}
        >
          PREVIOUS
        </button>
        <button
          className={
            "button next-button" +
            (index == infoArr.length - 1 ? " hidden" : "")
          }
          onClick={() => handleButtonClick(1)}
        >
          NEXT
        </button>
      </div>
    </aside>
  );
};

export default InfoWindowContent;
