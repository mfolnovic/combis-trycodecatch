import React, {PropTypes} from "react";
import MapContainer from "./MapContainter";
import LocationList from "./LocationList";
import RankUserContainer from "../containers/RankUserContainer";
import {Col, Grid, Row} from "react-bootstrap";
import Paper from "material-ui/Paper";
import Searchbar from "./Searchbar";
import {Card, CardActions, CardTitle, CardHeader, CardText} from 'material-ui/Card';

function LocationsWrapper({currentLocation, center, locations, google, loaded}) {
  const height = window.innerHeight - 48;
  return (
    <Grid fluid={true}>
      <Row>
        <Col sm={3} className="sidebar" style={{maxHeight: height + 'px'}}>
          <Card className="card">
            <CardTitle
              className="card-title"
              title="Amenities"
            />
            <CardText className="card-text">
              <LocationList locations={locations}/>
            </CardText>
          </Card>

          <Card className="card">
            <CardTitle
              className="card-title"
              title="Leaderboard"
            />
            <CardText className="card-text">
              <RankUserContainer />
            </CardText>
          </Card>
        </Col>
        <Col sm={9} className="map-container">
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
