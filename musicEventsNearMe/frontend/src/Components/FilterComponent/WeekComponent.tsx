import { useContext, useEffect, useState } from "react";
import DayOfWeekComponent from "./DayOfWeekComponent";
import { Context } from "../../Context/Context";
import { Filter } from "../../Interfaces/AppInterfaces";
import "./WeekComponent.css";
export const WeekComponent = () => {
  const [offSetTodaysDate, setOffSetTodaysDate] = useState(0);

  const { filter, setFilter }: any = useContext(Context);

  function handleDayFilterChange(event: Date): void {
    if (event) {
      console.log(filter);
      setFilter((prev: Filter) => {
        if (filter.startDate && !filter.endDate) {
          // Only startDate is initialized.
          const startDate =
            compareDates(filter.startDate, event) >= 0
              ? new Date(filter.startDate)
              : new Date(event);
          startDate.setHours(0, 0, 0);

          const endDate =
            compareDates(filter.startDate, event) >= 0
              ? new Date(event)
              : new Date(filter.startDate);
          endDate.setHours(23, 59, 59);

          return { startDate, endDate };
        } else if (!filter.startDate && !filter.endDate) {
          // Both are not initialized
          const startDate = new Date(event);
          startDate.setHours(0, 0, 0);

          const endDate = new Date(event);
          return { startDate, endDate };
        } else if (filter.startDate && filter.endDate) {
          // Both are initialized
          const startDate =
            compareDates(event, filter.endDate) === -1
              ? new Date(filter.startDate)
              : new Date(event);
          const endDate =
            compareDates(event, filter.endDate) === -1
              ? new Date(event)
              : new Date(filter.endDate);
          return { startDate, endDate };
        } else {
          // Handle other cases or return prev if none match
          return prev;
        }
      });
    }
  }
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
          handleDayFilterChange={handleDayFilterChange}
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

  function compareDates(startDate: Date, endDate: Date): number {
    const startDateCompare = new Date(startDate);
    startDateCompare.setHours(0, 0, 0, 0);

    const endDateCompare = new Date(endDate);
    endDateCompare.setHours(0, 0, 0, 0);

    if (startDateCompare < endDateCompare) {
      return 1;
    } else if (startDateCompare > endDateCompare) {
      return -1;
    } else {
      return 0;
    }
  }

  return (
    <><div className="week-container">
      <button className="week-selector" onClick={decrementWeek}>
        &lt;
      </button>
      {generateWeek()}
      <button className="week-selector" onClick={incrementWeek}>
        &gt;
      </button>
    </div></>
  );
};
