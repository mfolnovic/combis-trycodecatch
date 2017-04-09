import React, {Component} from "react";
import "./App.css";
import NearLocationsContainer from "./containers/NearLocationsContainer";
import Searchbar from "./components/Searchbar";
import {Col, Grid, Row} from "react-bootstrap";

class Layout extends Component {
  render() {
    return (
      <div className="App">
        <div className="App-header">
          <h1>Explorer</h1>
          <Searchbar />
        </div>
        <NearLocationsContainer />
      </div>
    );
  }
}

export default Layout;
