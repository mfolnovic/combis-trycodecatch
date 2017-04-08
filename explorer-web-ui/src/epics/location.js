import {combineEpics} from "redux-observable";
import {ajax} from "rxjs/observable/dom/ajax";
import {LOAD_MY_LOCATION, LOAD_NEAR_LOCATIONS, loadMyLocation, loadNearLocations, loadNearLocationsSuccess} from "../actions/location";
import {PATHS} from "../constants/api";
//
// export const loadMyLocationEpic = action$ =>
//   action$.ofType(LOAD_MY_LOCATION)
//       .filter(action => action.status === 'LOADING')
//       .mergeMap(action =>
//         navigator.geolocation.getCurrentPosition( position => {
//           loadMyLocation('SUCCESS', { lat: position.coords.latitude, lng: position.coords.longitude })
//         })
//       );

export const loadNearLocationsEpic = action$ =>
  action$.ofType(LOAD_NEAR_LOCATIONS)
    .mergeMap(action =>
      ajax({url: PATHS.location(action.lat, action.lng), responseType: 'json', crossDomain: true})
        .map(response => loadNearLocationsSuccess(response))
    );

export const locationEpic = combineEpics(
  // loadMyLocationEpic,
  loadNearLocationsEpic,
);
