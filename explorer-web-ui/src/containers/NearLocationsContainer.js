import React, {Component} from "react";
import {connect} from "react-redux";
import {loadNearLocations} from "../actions/location";
import LocationsWrapper from "../components/LocationsWrapper";

class NearLocationsContainer extends Component {
  constructor(props) {
    super(props);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.myLocation !== this.props.myLocation && nextProps.myLocation !== null) {
      this.props.dispatch(loadNearLocations(nextProps.myLocation.lat, nextProps.myLocation.lng));
    }
  }

  render() {
    return <LocationsWrapper currentLocation={this.props.myLocation} locations={this.props.nearLocations}/>;
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    nearLocations: state.location.near,
    myLocation: state.location.my,
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
