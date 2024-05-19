import React, { useEffect, useState } from "react";
import "./DayOfWeekComponent.css";
interface DayOfWeekComponentProps {
  className: any;
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
    <button onClick={handleDayFilterChange} className={props.className + ' day-of-week-button'}>
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
