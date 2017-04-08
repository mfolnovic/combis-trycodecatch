import React, {PropTypes} from "react";
import Location from "./Location";

function LocationList({locations}) {
  return (
    <div>
      {locations.map(location =>
        <Location key={location.name} location={location}/>
      )}
    </div>
  );
}

LocationList.propTypes = {
  locations: PropTypes.array.isRequired,
};

export default LocationList;