import React, {PropTypes} from "react";
import Location from "./Location";

function LocationList({locations}) {
  return (
    <div>
      {locations.map(location =>
        <Location key={location.name} user={location}/>
      )}
    </div>
  );
}

LocationList.propTypes = {
  users: PropTypes.array.isRequired,
};

export default LocationList;