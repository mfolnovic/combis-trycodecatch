import React, {PropTypes} from "react";
import MapContainer from "./MapContainter";
import LocationList from "./LocationList";

function LocationsWrapper({currentLocation, locations}) {
  return (
    <div>
      <MapContainer currentLocation={currentLocation} locations={locations} />
      <LocationList locations={locations}/>
    </div>
  );
}

LocationsWrapper.propTypes = {
  currentLocation: PropTypes.object,
  locations: PropTypes.array.isRequired
};

export default LocationsWrapper;