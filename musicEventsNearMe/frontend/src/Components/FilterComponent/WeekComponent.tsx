import { useContext, useEffect, useState } from "react";
import DayOfWeekComponent from "./DayOfWeekComponent";
import { Context } from "../../Context/Context";
import { Filter } from "../../Interfaces/AppInterfaces";

import "./WeekComponent.css";
export const WeekComponent = (props: any) => {
  const [offSetTodaysDate, setOffSetTodaysDate] = useState(0);

  const { filter, setFilter }: any = useContext(Context);

  useEffect(() => {
    console.log(filter.reset)
    if (filter.reset == true) {
      setOffSetTodaysDate(0);
      generateWeek()
      setFilter((prev: Filter) => ({
        ...prev,
        reset: false,
      }));
    }
  }, [filter.reset])

  const generateWeek = () => {
    const elementArray = [];
    for (let dateIndex = 0; dateIndex < 4; dateIndex++) {
      elementArray.push(
        <DayOfWeekComponent
          className={
            getDayMonthYear(addDaysToDate(dateIndex + offSetTodaysDate)) ===
              getDayMonthYear(filter.startDate) ||
            getDayMonthYear(addDaysToDate(dateIndex + offSetTodaysDate)) ===
              getDayMonthYear(filter.endDate)
              ? "highlight"
              : ""
          }
          key={dateIndex}
          handleDayFilterChange={props.handleDayFilterChange}
          date={addDaysToDate(dateIndex + offSetTodaysDate)}
        ></DayOfWeekComponent>
      );
    }
    return elementArray;
  };

  const getDayMonthYear = (currentDate: Date) => {
    const day = currentDate.getDate();
    const month = currentDate.getMonth() + 1; // Months are zero-based, so add 1
    const year = currentDate.getFullYear();

    return `${year}-${day}-${month}`;
  };

  const addDaysToDate = (number: number): Date => {
    const currentDate = new Date();
    currentDate.setDate(currentDate.getDate() + number);
    return currentDate;
  };

  const decrementWeek = () => {
    setOffSetTodaysDate((prev) => prev - 4);
  };

  const incrementWeek = () => {
    setOffSetTodaysDate((prev) => prev + 4);
  };



  return (
    <><div className="week-container">
      <button className="week-selector previous" onClick={decrementWeek}/>
      {generateWeek()}
      <button className="week-selector next" onClick={incrementWeek} />
    </div></>
  );
};
