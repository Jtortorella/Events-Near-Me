import './SearchBarStyles.css'

import { useContext } from "react";
import { get } from "../../Services/APIService";
import { KeywordSearchHitContainer } from "./KeywordSearchHitContainer";
import { ConcertDataContext } from "../../Context/Context";
import React from 'react';


function SearchBarComponent() {
  const { setKeyWordSearchResponse }: any = useContext(ConcertDataContext);

  async function checkDatabaseForMatchingTerms(KeyWord: string): Promise<any> {
    if (KeyWord.trim() != "") {
      setKeyWordSearchResponse(await get({ url: `http://localhost:8080/api/search/${KeyWord}` }));
    }
    else {
      setKeyWordSearchResponse([]);
    }
  }

  return (
    <>
    <div className="search-bar-component-container">
      <div className='search-bar-container'>
      <input
        className="search-bar"
        type="input"
        placeholder='e.g. Genre, Artist, Location'
        onChange={(inputChange) =>
          checkDatabaseForMatchingTerms(inputChange.target.value)
        }
      >
      </input>
      <i className="SEARCH icon"></i>
      </div>

      <KeywordSearchHitContainer/>
      </div>
    </>
  );
}

export default SearchBarComponent;
