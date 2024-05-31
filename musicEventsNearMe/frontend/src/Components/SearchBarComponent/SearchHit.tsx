import './SearchBarStyles.css'

function SearchHit(props: any) {
    function handleSearchHitClick() {
        console.log(props.value + "was clicked");

    }

  return (
    <div className="search-hit-anchor-tag-container">
        <a className="search-hit" onClick={(e) => handleSearchHitClick()}>{props.value}</a>
        <i className={props.class + ' icon'}></i>
    </div>
  )
}

export default SearchHit