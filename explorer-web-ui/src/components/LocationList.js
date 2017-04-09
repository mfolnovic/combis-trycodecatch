import React, {PropTypes} from "react";
import Location from "./Location";
import {List} from 'material-ui/List';
import Divider from 'material-ui/Divider';

function LocationList({locations}) {
  let items = [];
  locations.forEach((location, i) => {
    items.push(<Location key={location.name} location={location}/>);
    if (i + 1 < locations.length) {
      items.push(<Divider key={'div' + i} inset={false}/>);
    }
  });
  return (
    <List>
      {items}
    </List>
  );
}

LocationList.propTypes = {
  locations: PropTypes.array.isRequired,
};

export default LocationList;
