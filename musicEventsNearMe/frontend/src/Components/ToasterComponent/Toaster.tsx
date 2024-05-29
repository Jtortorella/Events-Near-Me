import { useContext } from "react";
import { ConcertDataContextProps, Context } from "../../Context/Context";
import './ToasterStyles.css'
function Toaster() {
  const props: ConcertDataContextProps | undefined = useContext(Context);

  setTimeout(() => {
    props?.setIsError(false);
  }, 5000);

  const handleClick = () => {
    props?.setIsError(false);
  };

  return props?.isError ? (
    <div className="bottom-right center toaster" onClick={handleClick}>
      <h3>"Error!"</h3>
      <p>Oops, something went wrong. Please try again later.</p>
      
</div>
  ) : null;
}

export default Toaster;
