import React, {Component, PropTypes} from "react";
import ReactDOM from "react-dom";
import {connect} from "react-redux";
import {setNewCenter} from "../actions/location";
import TextField from "material-ui/TextField";
import {GoogleApiWrapper} from "google-maps-react";
import {blue100, blue50, blue150, orange500} from "material-ui/styles/colors";
import ActionSearch from 'material-ui/svg-icons/action/search';

const styles = {
  underlineStyle: {
    borderColor: blue50,
  },
  underlineFocusStyle: {
    borderColor: blue150,
  },
  floatingLabelStyle: {
    color: blue50,
  },
  floatingLabelFocusStyle: {
    color: blue50,
  },
  styles: {
    color: blue50,
    width: '500px',
  },
  inputStyle: {
    color: blue50,
    width: '500px',
  },
  hintStyle: {
    color: blue50,
    width: '500px',
  }
};

class Searchbar extends Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.renderAutoComplete();

    const aref = this.refs.search;
    const node = ReactDOM.findDOMNode(aref).querySelector(':scope > input');
    node.focus();
  }

  componentDidUpdate() {
    this.renderAutoComplete();
  }

  renderAutoComplete() {
    if (!this.props.google) return;

    const aref = this.refs.search;
    const node = ReactDOM.findDOMNode(aref).querySelector(':scope > input');
    node.placeholder = "";

    let options = {
      types: ['(cities)'],
      // componentRestrictions: {country: "us"}
    }

    let autocomplete = new this.props.google.maps.places.Autocomplete(node, options);

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
    //      <input
    // ref='autocomplete'
    // type="text"
    //   />

    return (
      <div>
        <ActionSearch color={blue50} />
        <TextField
          hintText="Search by name of location..."
          ref="search"
          style={styles.style}
          inputStyle={styles.inputStyle}
          hintStyle={styles.hintStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
          underlineStyle={styles.underlineStyle}
          // floatingLabelStyle={styles.floatingLabelStyle}
          // floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          // floatingLabelText="Search"
        />
      </div>
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
)(GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places', 'visualization']
})(Searchbar))
