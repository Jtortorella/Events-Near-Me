import React, { useEffect, useState } from "react";
import "./DayOfWeekComponent.css"
interface DayOfWeekComponentProps {
  date: Date;
  handleDayFilterChange: any;
}

const DayOfWeekComponent: React.FC<DayOfWeekComponentProps> = (props) => {
  const [formattedDate, setFormattedDate] = useState("");
  const [formattedMonth, setFormattedMonth] = useState("");
  const [formattedDay, setFormattedDay] = useState("");

  useEffect(() => {
    const date = new Date(props.date);
    setFormattedDate(date.toLocaleDateString("en-US", { weekday: "short" }));
    setFormattedMonth(date.toLocaleDateString("en-US", { month: "short" }));
    setFormattedDay(date.toLocaleDateString("en-US", { day: "numeric" }));
  }, [props.date]);

  const handleDayFilterChange = () => {
    props.handleDayFilterChange(props.date);
  };

  return (
      <button onClick={handleDayFilterChange}>
            <div className="day-of-week-container">

        <div className="day-of-week-text">
          {formattedDate}
          <br />
          {formattedMonth} {formattedDay}
        </div>
        </div>

      </button>
  );
};

export default DayOfWeekComponent;
