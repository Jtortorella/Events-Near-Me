import "./SearchBarStyles.css";
import { KeyWordResult } from "../../Interfaces/AppInterfaces";
import { ConcertDataContext } from "../../Context/Context";
import { useContext } from "react";
import React from "react";

function SearchHit({value, keyWordType}: KeyWordResult) {
  const { setActiveKeyWord, setInfoArr }: any = useContext(ConcertDataContext);

  async function handleSearchHitClick() {
    setActiveKeyWord({value, keyWordType})
    setInfoArr([])
  }

  return (
    <div className="search-hit-anchor-tag-container">
      <a className="search-hit" onClick={() => handleSearchHitClick()}>
        {value}
      </a>
      <i className={keyWordType + " icon"}></i>
    </div>
  );
}

export default SearchHit;
