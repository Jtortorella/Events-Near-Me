import React, { useState, useEffect, useContext } from "react";
import { convertLocalDateTimeToDateTime } from "../../Hooks/HandleDateTime";
import { EventInfo } from "../../Interfaces/AppInterfaces";
import { getEventDetails } from "../../Hooks/APICall";
import { Context } from "../../Context/Context";

const InfoWindowContent: React.FC = () => {
  const [infoArr, setInfoArr] = useState<EventInfo[]>([]);
  const [index, setIndex] = useState<number>(0);
  const { selectedEventId }: any = useContext(Context);

  useEffect(() => {
    if (selectedEventId != -1) {
      const fetchData = async () => {
        try {
          const resolvedInfo = await getEventDetails(selectedEventId);
          if (resolvedInfo) {
            setInfoArr(resolvedInfo);
          }
        } catch (error) {
          console.error("Error fetching event info:", error);
        }
      };
      fetchData();
    }
  }, [selectedEventId]);

  if (!infoArr || infoArr.length === 0) {
    return null;
  }

  const { name, startDate, performer, location } = infoArr[index];

  const handleButtonClick = (direction: number): void => {
    setIndex((prev) => (prev += direction));
  };

  return (
    selectedEventId != -1 &&
    <aside id="infoWindowContainer">
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
