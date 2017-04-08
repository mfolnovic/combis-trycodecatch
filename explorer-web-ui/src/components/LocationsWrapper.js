import React, {PropTypes} from "react";
import MapContainer from "./MapContainter";
import LocationList from "./LocationList";
import {Grid, Row, Col} from "react-bootstrap";
import {GoogleApiWrapper} from "google-maps-react";
import Paper from 'material-ui/Paper';
import Searchbar from './Searchbar';

function LocationsWrapper({currentLocation, locations, google, loaded}) {
  return (
    <Grid>
      <Row>
        <Col sm={6} smPush={3} style={{margin: "15px 0px 80px 0px"}}>
          <Searchbar google={google}/>
        </Col>
      </Row>
      <Row>
        <Col sm={8}>
          <Paper zDepth={2} style={{width: "100%", height: "550px"}}>
            <MapContainer currentLocation={currentLocation} locations={locations} google={google} loaded={loaded} />
          </Paper>
        </Col>
        <Col sm={4}>
          <Paper zDepth={1}>
            <LocationList locations={locations}/>
          </Paper>
        </Col>
      </Row>
    </Grid>
  );
}

LocationsWrapper.propTypes = {
  currentLocation: PropTypes.object,
  locations: PropTypes.array.isRequired
};

export default GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places','visualization']
})(LocationsWrapper);