import { useContext } from "react";
import { WeekComponent } from "./WeekComponent";
import { Context } from "../../Context/Context";
import { Filter } from "../../Interfaces/AppInterfaces";
function FilterComponent() {
  const { filter, setFilter, setSelectedEventId }: any = useContext(Context);

  function resetFilter(): void {
    setSelectedEventId(-1);
    setFilter({
      startDate: new Date(),
      endDate: new Date(),
    });
  }
  
  function handleDayFilterChange(event: Date): void {
    if (event) {
      setSelectedEventId(-1);
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
    <div className="wrapper">
      <div className="container">
        <WeekComponent handleDayFilterChange={handleDayFilterChange}></WeekComponent>
        <button onClick={resetFilter}>Reset Filter</button>
      </div>
    </div>
  );
}

export default FilterComponent;
