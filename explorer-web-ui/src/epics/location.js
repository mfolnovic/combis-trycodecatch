import {ajax} from "rxjs/observable/dom/ajax";
import {LOAD_MY_LOCATION, LOAD_NEAR_LOCATIONS, loadMyLocation, loadNearLocations} from "../actions/location";
import {PATHS} from "../constants/api";

export const loadMyLocationEpic = action$ =>
  action$.ofType(LOAD_MY_LOCATION)
      .filter(action => action.status === 'LOADING')
      .mergeMap(action =>
        navigator.geolocation.getCurrentPosition( position => {
          loadMyLocation('SUCCESS', { lat: position.coords.latitude, lng: position.coords.longitude })
        })
      );

export const loadNearLocationsEpic = action$ =>
  action$.ofType(LOAD_NEAR_LOCATIONS)
    .filter(action => action.status === 'LOADING')
    .mergeMap(action =>
      ajax({url: PATHS.location(action.lat, action.lng), responseType: 'json', crossDomain: true})
        .map(response => loadNearLocations('SUCCESS', action.lat, action.lng, response))
    );