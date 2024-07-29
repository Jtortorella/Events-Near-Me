import "./SearchBarStyles.css";

import { useContext, useEffect } from "react";
import { ConcertDataContext } from "../../Context/Context";
import SearchHit from "./SearchHit";
import { KeyWordResult } from "../../Interfaces/AppInterfaces";
import React from "react";

export function KeywordSearchHitContainer() {
  const { keyWordSearchResponse }: any = useContext(ConcertDataContext);
  useEffect(() => {}, [keyWordSearchResponse]);

  return (
    keyWordSearchResponse &&
    keyWordSearchResponse.length > 0 && (
      <div className="search-hits-container">
        {keyWordSearchResponse.map((keyWordResult: KeyWordResult, idx: number) => (
          <SearchHit
            key={keyWordResult.value + idx} // Assuming 'value' is unique. Otherwise, use a unique identifier.
            value={keyWordResult.value}
            keyWordType={keyWordResult.keyWordType}
          />
        ))}
      </div>
    )
  );
}
