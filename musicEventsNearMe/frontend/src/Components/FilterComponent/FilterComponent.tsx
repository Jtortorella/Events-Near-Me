import { useContext } from 'react';
import {WeekComponent} from './WeekComponent';
import { Context } from '../../Context/Context';
function FilterComponent() {
  const { filter }: any = useContext(Context);
  return (
    <div className="wrapper">
        <div className="container">

         {filter.startDate.toString()}
          {filter.endDate.toString()}
            <WeekComponent></WeekComponent>
        </div>
    </div>
  )
}

export default FilterComponent