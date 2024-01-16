import React, { useState, useEffect } from "react";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";
import { EventInfo, Performer } from "../../Interfaces/AppInterfaces";

interface InfoWindowContentProps {
  eventInfo: Promise<EventInfo[] | undefined>;
}

const InfoWindowContent: React.FC<InfoWindowContentProps> = ({ eventInfo }) => {
  const [infoArr, setInfoArr] = useState<EventInfo[]>([]);
  const [index, setIndex] = useState<number>(0);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const resolvedInfo = await eventInfo;
        if (resolvedInfo) {
          setInfoArr([...resolvedInfo]);
        }
      } catch (error) {
        console.error("Error fetching event info:", error);
      }
    };

    fetchData();
  }, [eventInfo]);

  if (!infoArr || infoArr.length === 0) {
    console.log("INCORRECT DATA");
    return null;
  }

  const { name, start_date, performers_details, location_name } = infoArr[index];

  const performers: Performer[] = performers_details.split('|').map((value) => {
    const [id, performerName, url, numUpcomingEvents] = value.split(',');
    return {
      id,
      name: performerName,
      url,
      numUpcomingEvents: parseInt(numUpcomingEvents),
    };
  });

  const handleButtonClick = (direction: number) => {
    setIndex((prev) => Math.max(0, Math.min(prev + direction, infoArr.length - 1)));
  };

  return (
    <div className="infoWindowContainer">
      {name}
      <br />
      {convertLocalDateTimeToDateTime(start_date)}
      <br />
      <br />
      PERFORMERS:
      {performers.map((performer, idx) => (
        <p key={idx + 1}>
          {idx + 1}: {performer.name} <br />
        </p>
      ))}
      <br />
      {location_name}
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
