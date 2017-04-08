import React, {Component, PropTypes} from "react";
import ReactDOM from 'react-dom'

class Searchbar extends Component {
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

    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace();

      if (!place.geometry) {
        return;
      }
      console.log(place.geometry.location.lat());
    })
  }

  render() {
    return (
      <input
        ref='autocomplete'
        type="text"></input>
    );
  }
}

export default Searchbar;