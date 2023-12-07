import { useContext } from "react";
import { ConcertDataContextProps, Context } from "../../Context/Context";
import "../../Styles/WheelStyles.css";

function LoadingWheel() {
    const props: ConcertDataContextProps | undefined = useContext(Context);

    return (
        props?.isLoading && (
            <div className="wheelContainer center">
                <div className="wheel"></div>
            </div>
        )
    );
}

export default LoadingWheel;
