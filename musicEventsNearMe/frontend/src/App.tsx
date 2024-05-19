import React from "react";
import { Provider } from "./Context/Context";
import Toaster from "./Components/ToasterComponent/Toaster";
import LoadingWheel from "./Components/LoadingWheel/LoadingWheel";
import FilterComponent from "./Components/FilterComponent/FilterComponent";
import LeafletMapComponent from "./Components/MapComponents/LeafletMapComponent";

const App: React.FC = () => {
  return (
    <Provider>
      <FilterComponent />
      <Toaster />
      <LoadingWheel />
      <LeafletMapComponent />
    </Provider>
  );
};

export default App;
