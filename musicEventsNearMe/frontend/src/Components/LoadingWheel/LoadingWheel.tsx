import { useContext } from "react";
import { ConcertDataContext } from "../../Context/Context";
import "../../Styles/WheelStyles.css";
import React from "react";

function LoadingWheel() {
  const props: any = useContext(ConcertDataContext);

  return (
    props?.isLoading && (
      <div className="center">
        <div className="lds-facebook">
          <div></div>
          <div></div>
          <div></div>
        </div>
      </div>
    )
  );
}

export default LoadingWheel;
