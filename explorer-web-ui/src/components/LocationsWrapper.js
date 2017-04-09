import React, {PropTypes} from "react";
import MapContainer from "./MapContainter";
import LocationList from "./LocationList";
import RankUserContainer from "../containers/RankUserContainer";
import {Col, Grid, Row} from "react-bootstrap";
import Paper from "material-ui/Paper";
import Searchbar from "./Searchbar";

function LocationsWrapper({currentLocation, center, locations, google, loaded}) {
  const height = window.innerHeight - 48;
  return (
    <Grid fluid={true}>
      <Row>
        <Col sm={4}>
          <Paper zDepth={1}>
            <LocationList locations={locations}/>
          </Paper>

          <Paper zDepth={1}>
            <RankUserContainer />
          </Paper>
        </Col>
        <Col sm={8} className="map-container">
          {/*<Paper zDepth={2} style={{width: "100%", height: height + "px"}}>*/}
            <MapContainer currentLocation={currentLocation}
                          center={center}
                          locations={locations}
                          google={google}
                          loaded={loaded}
                          height={height}/>
          {/*</Paper>*/}
        </Col>
      </Row>
    </Grid>
  );
}

LocationsWrapper.propTypes = {
  currentLocation: PropTypes.object,
  locations: PropTypes.array.isRequired
};

export default LocationsWrapper;
