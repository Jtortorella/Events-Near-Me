import { useContext } from "react";
import { ConcertDataContext} from "../../Context/Context";
import './ToasterStyles.css'
import React from "react";
function Toaster() {
  const { setIsError, isError, errorMessage }: any = useContext(ConcertDataContext);

  setTimeout(() => {
    setIsError(false);
  }, 5000);

  const handleClick = () => {
    setIsError(false);
  };

  return isError == true && (
    <div className="bottom-right center toaster" onClick={handleClick}>
      <h3>"Error!"</h3>
      <p>Oops, something went wrong. Please try again later.</p>
      <p>{errorMessage}</p>
  </div>
  ) 
}

export default Toaster;
