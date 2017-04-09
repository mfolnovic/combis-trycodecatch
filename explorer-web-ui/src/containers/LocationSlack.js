import React, {Component} from "react";
import {connect} from "react-redux";
import { loadLocation } from "../actions/location";
import RaisedButton from 'material-ui/RaisedButton';

class LocationSlack extends Component {
  componentWillReceiveProps(nextProps) {
    console.debug(nextProps);
    if (nextProps.myLocation !== this.props.myLocation && nextProps.myLocation !== null) {
      this.props.dispatch(loadLocation(nextProps.myLocation.city, nextProps.myLocation.lat, nextProps.myLocation.lng));
    }
  }

  render() {
    if (this.props.loaded == null) {
      return null;
    }

    return (
      <RaisedButton
        className="locationSlack"
        label={'Slack for ' + this.props.loaded.name}
                    onTouchTap={() => window.open("https://trycodecatch-explorer.slack.com/messages/" + this.props.loaded.channel.slack_id +"/")} />
    );
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    myLocation: state.location.my,
    loaded: state.location.loaded,
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
)(LocationSlack);
