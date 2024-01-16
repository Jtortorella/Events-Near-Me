import { useContext } from "react";
import { WeekComponent } from "./WeekComponent";
import { Context } from "../../Context/Context";
function FilterComponent() {
  const { filter, setFilter }: any = useContext(Context);

  function resetFilter(): void {
    setFilter({
      startDate: new Date(),
      endDate: new Date(),
    });
  }

  return (
    <div className="wrapper">
      <div className="container">
        <p>
        {filter.startDate.toString()}
        </p>
        <p>
        {filter.endDate.toString()}
        </p>
        <WeekComponent></WeekComponent>
        <button onClick={resetFilter}>Reset Filter</button>
      </div>
    </div>
  );
}

export default FilterComponent;
