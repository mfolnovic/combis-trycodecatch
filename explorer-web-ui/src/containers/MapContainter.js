import React, {Component} from "react";
import {connect} from "react-redux";
import Map, {GoogleApiWrapper, Marker} from 'google-maps-react'

class MapContainer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentPosition : null
    };
  }

  componentWillMount() {
    navigator.geolocation.getCurrentPosition(this.onPositionGet);
  }

  onPositionGet = (position) => {
    this.setState({
      currentPosition : {
        lng: position.coords.longitude,
        lat: position.coords.latitude
      }
    })
  };

  render() {
    if (!this.props.loaded) {
      return <div>Loading...</div>
    }

    let meMarker = "";

    if (this.state.currentPosition) {
      meMarker = <Marker
        name={'You\'re here'}
        position={this.state.currentPosition}/>
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

const mapStateToProps = (state) => {
  return {
    users: state.users,
  }
};

export default connect(
  mapStateToProps,
)(GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places','visualization']
})(MapContainer));