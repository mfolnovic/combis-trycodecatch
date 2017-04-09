import React, {Component} from "react";
import {connect} from "react-redux";
import {loadNearAmenities} from "../actions/amenities";
import LocationsWrapper from "../components/LocationsWrapper";

class NearLocationsContainer extends Component {
  constructor(props) {
    super(props);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.centerLocation !== this.props.centerLocation && nextProps.centerLocation !== null) {
      this.props.dispatch(loadNearAmenities(nextProps.centerLocation.lat, nextProps.centerLocation.lng));
    }
  }

  render() {
    return <LocationsWrapper currentLocation={this.props.myLocation}
                             locations={this.props.nearLocations}
                             center={this.props.centerLocation} />;
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    nearLocations: state.amenity.near || [],
    myLocation: state.location.my,
    centerLocation: state.location.center || state.location.my,
  }
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    dispatch: dispatch,
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NearLocationsContainer);
