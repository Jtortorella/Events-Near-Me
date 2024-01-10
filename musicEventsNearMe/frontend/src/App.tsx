import React, {  } from "react";
import MapComponent from "./Components/MapComponents/MapComponent";
import { Provider } from "./Context/Context";
import Toaster from "./Components/ToasterComponent/Toaster";
import LoadingWheel from "./Components/LoadingWheel/LoadingWheel";
import FilterComponent from "./Components/FilterComponent/FilterComponent";

const App: React.FC = () => {
  return (
    <Provider>
      <FilterComponent />
      <MapComponent />
      <Toaster />
      <LoadingWheel />
    </Provider>
  );
};

export default App;
