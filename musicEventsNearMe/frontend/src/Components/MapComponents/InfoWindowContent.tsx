import React, { useState, useEffect } from "react";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";
import { EventInfo } from "../../Interfaces/AppInterfaces";
import { getEventDetails } from "../../Hooks/APICall";

interface InfoWindowContentProps {
  musicEventId: number | number[];
}

const InfoWindowContent: React.FC<InfoWindowContentProps> = ({ musicEventId }) => {
  const [infoArr, setInfoArr] = useState<EventInfo[]>([]);
  const [index, setIndex] = useState<number>(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resolvedInfo = await getEventDetails(musicEventId);
        if (resolvedInfo) {
          setInfoArr(resolvedInfo);
        }
      } catch (error) {
        console.error("Error fetching event info:", error);
      }
    };
    fetchData();
  }, [musicEventId]);

  if (!infoArr || infoArr.length === 0) {
    return null;
  }

  const { name, startDate, performer, location } = infoArr[index];

  const handleButtonClick = (direction: number): void => {
    setIndex((prev) => Math.max(0, Math.min(prev + direction, infoArr.length - 1)));
  };

  return (
    <div className="infoWindowContainer">
      {name}
      <br />
      {convertLocalDateTimeToDateTime(startDate)}
      <br />
      <br />
      PERFORMERS:
      {performer.map((performer, idx) => (
        <p key={idx + 1}>
          {idx + 1}: {performer.name} <br />
        </p>
      ))}
      <br />
      {location.name}
      <br />
      <div id="button-container">
        {index !== 0 && (
          <button className="button prev-button" onClick={() => handleButtonClick(-1)}>
            PREVIOUS
          </button>
        )}
        {index !== infoArr.length - 1 && (
          <button className="button next-button" onClick={() => handleButtonClick(1)}>
            NEXT
          </button>
        )}
      </div>
    </div>
  );
};

export default InfoWindowContent;
