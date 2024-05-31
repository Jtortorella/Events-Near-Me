import './SearchBarStyles.css'

import { useContext, useEffect } from "react";
import { Context } from "../../Context/Context";
import SearchHit from "./SearchHit";

export function KeywordSearchHitContainer() {

    const { keyWordSearchResponse }: any = useContext(Context);
    useEffect(() => {
        renderSearchBarHits()
    }, [keyWordSearchResponse])

    function renderSearchBarHits() {



        return keyWordSearchResponse.map((value: string) => (
            <SearchHit 
                value={value.split(', ')[0]}
                class={value.split(', ')[1]}
            />
          ))

    }
    return (
        <div className={keyWordSearchResponse.length > 0 ? "search-hits-container" : ""}>

        {renderSearchBarHits()}
        </div>

    );
  }


