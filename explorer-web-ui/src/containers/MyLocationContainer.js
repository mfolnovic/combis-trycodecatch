import React, {Component} from "react";
import {connect} from "react-redux";
import { loadMyLocation } from "../actions/location";

class MyLocationContainer extends Component {
  constructor(props) {
    super(props);
  }

  componentWillMount() {
    navigator.geolocation.getCurrentPosition(function (position) {
      this.props.dispatch(loadMyLocation({lat: position.coords.latitude, lng: position.coords.longitude}));
    }.bind(this));
  }

  render() {
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
)(MyLocationContainer);
