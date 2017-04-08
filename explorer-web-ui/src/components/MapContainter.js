import React, {Component, PropTypes} from "react";
import Map, {GoogleApiWrapper, Marker} from 'google-maps-react'

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
          name={'You\'re here'}
          position={this.props.currentLocation}/>
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
           style={{}}
           zoom={14}
           containerStyle={{width: '450px', height: '450px', position: 'relative'}}
           centerAroundCurrentLocation={true} >
        {markers}
      </Map>
    )
  }
}

MapContainer.propTypes = {
  currentLocation: PropTypes.object,
  locations: PropTypes.array.isRequired
};

export default GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places','visualization']
})(MapContainer);