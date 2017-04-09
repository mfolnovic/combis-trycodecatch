import {Component} from "react";
import {connect} from "react-redux";
import { loadMyLocation } from "../actions/location";
import {GoogleApiWrapper} from "google-maps-react";

class MyLocationContainer extends Component {
  componentDidMount() {
    navigator.geolocation.getCurrentPosition(function (position) {
      let geocoder = new this.props.google.maps.Geocoder();
      var latlng = new this.props.google.maps.LatLng(position.coords.latitude, position.coords.longitude);

      geocoder.geocode( { 'location': latlng }, function(results, status) {
        if (status == 'OK') {
          let city = 'unknown';

          for (var i in results) {
            if (results[i].types.indexOf('locality') !== -1) {
              city = results[i].formatted_address;
            }

            this.props.dispatch(loadMyLocation({lat: position.coords.latitude, lng: position.coords.longitude, city: city}));
          }
        }
      }.bind(this));
    }.bind(this));
  }

  render() {
    console.debug(this.props);
    return this.props.children;
  }
}

const mapStateToProps = (state, ownProps) => {
  return {}
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    dispatch: dispatch,
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places', 'visualization', 'geocoding']
})(MyLocationContainer));
