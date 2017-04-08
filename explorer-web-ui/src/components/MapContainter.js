import React, {Component, PropTypes} from "react";
import Map, {Marker} from 'google-maps-react'
import HomeIcon from "../static/images/home_scaled.png"

class MapContainer extends Component {
  render() {
    if (!this.props.loaded) {
      return <div>Loading...</div>
    }

    let markers = [];

    if (this.props.currentLocation) {
      markers = [
        ...markers,
        <Marker
          key="current"
          name={'You\'re here'}
          position={this.props.currentLocation}
          icon={HomeIcon}/>
      ]
    }

    if (this.props.locations.length > 0) {
      markers = [
        ...markers,
        ...this.props.locations.map(
          location => <Marker key={location.name} name={location.name} position={{lat: location.latitude, lng: location.longitude}}/>
        )
      ]
    }
    return (
      <Map google={this.props.google}
           className={'map'}
           zoom={14}
           containerStyle={{width: '100%', height: '550px', position: 'relative'}}
           center={this.props.center}
           centerAroundCurrentLocation={true} >
        {markers}
      </Map>
    )
  }
}

MapContainer.propTypes = {
  currentLocation: PropTypes.object,
  locations: PropTypes.array.isRequired,
  center: PropTypes.object
};

export default MapContainer;