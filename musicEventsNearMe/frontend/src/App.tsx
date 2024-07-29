import React from "react";
import { ConcertDataProvider } from "./Context/Context";
import Toaster from "./Components/ToasterComponent/Toaster";
import LoadingWheel from "./Components/LoadingWheel/LoadingWheel";
import FilterComponent from "./Components/FilterComponent/FilterComponent";
import LeafletMapComponent from "./Components/MapComponents/LeafletMapComponent";
import SearchBarComponent from "./Components/SearchBarComponent/SearchBarComponent";

const App: React.FC = () => {
  return (
    <ConcertDataProvider>
      <SearchBarComponent />
      <FilterComponent />
      <Toaster />
      <LoadingWheel />
      <LeafletMapComponent />
    </ConcertDataProvider>
  );
};

export default App;
