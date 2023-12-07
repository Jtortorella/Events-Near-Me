import { useContext } from "react";
import { ConcertDataContextProps, Context } from "../../Context/Context";

function Toaster() {
  const props: ConcertDataContextProps | undefined = useContext(Context);

  setTimeout(() => {
    props?.setIsError(false);
  }, 5000);

  const handleClick = () => {
    props?.setIsError(false);
  };

  return props?.isError ? (
    <div className="center" onClick={handleClick}>
      {"Error!"}
    </div>
  ) : null;
}

export default Toaster;
