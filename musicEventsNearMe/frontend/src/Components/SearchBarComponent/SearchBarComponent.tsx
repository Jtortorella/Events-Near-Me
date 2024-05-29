
function SearchBarComponent() {
    function checkDatabaseForMatchingTerms(e : any): any {
        
    }

  return (
    <><h2>SearchBarComponent</h2><input type="input" onChange={(e) => checkDatabaseForMatchingTerms(e)}></input></>
  )
}

export default SearchBarComponent