import React, {Component, PropTypes} from "react";
import ReactDOM from 'react-dom'
import {connect} from "react-redux";
import {setNewCenter} from '../actions/location';

class Searchbar extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.renderAutoComplete();
  }

  componentDidUpdate() {
    this.renderAutoComplete();
  }

  renderAutoComplete() {
    if (!this.props.google) return;

    const aref = this.refs.autocomplete;
    const node = ReactDOM.findDOMNode(aref);
    let autocomplete = new this.props.google.maps.places.Autocomplete(node);

    let me = this;
    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace();

      if (!place.geometry) {
        return;
      }
      me.props.dispatch(setNewCenter({lat: place.geometry.location.lat(), lng: place.geometry.location.lng()}));
    })
  }

  render() {
    return (
      <input
        ref='autocomplete'
        type="text"
      />
    );
  }
}

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    dispatch: dispatch,
  };
};

export default connect(
  mapDispatchToProps
)(Searchbar);