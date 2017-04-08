import React, {Component, PropTypes} from "react";
import {connect} from "react-redux";
import Map, {GoogleApiWrapper, Marker} from 'google-maps-react'

class MapContainer extends Component {
  render() {
    if (!this.props.loaded) {
      return <div>Loading...</div>
    }

    let meMarker = "";

    if (this.props.currentLocation) {
      meMarker = <Marker
        name={'You\'re here'}
        position={this.props.currentLocation}/>
    }

    return (
      <Map google={this.props.google}
           className={'map'}
           style={{}}
           zoom={14}
           containerStyle={{width: '450px', height: '450px', position: 'relative'}}
           centerAroundCurrentLocation={true} >
        {meMarker}
      </Map>
    )
  }
}

MapContainer.propTypes = {
  currentLocation: PropTypes.object.isRequired,
  locations: PropTypes.array.isRequired
};

export default GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places','visualization']
})(MapContainer);