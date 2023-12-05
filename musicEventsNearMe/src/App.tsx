import React, { useEffect, useState } from "react";
import MapComponent from "./Components/MapComponents/MapComponent";
import { get } from "./Hooks/APIService";

const App: React.FC = () => {
  const apiKey = "AIzaSyCww45TrPXdk_vRa4sKRU-8Gzcd9jr6J2M";
  const [eventData, setEventData] = useState([]);
  const [locationDetails, setLocationDetails] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const eventDataResponse = await get({
          url: "http://localhost:8080/concertData/events",
        });
        setEventData(eventDataResponse['events']);
      } catch (err) {
        console.log(err);
      }
    };
    fetchData();
    console.log(eventData);
  }, []); // Empty dependency array ensures the effect runs once when the component mounts

  return (
    <div>
      <h1>My React Map App</h1>
      <MapComponent eventData={eventData} apiKey={apiKey} />
    </div>
  );
};

export default App;
