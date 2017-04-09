import {combineEpics} from "redux-observable";
import {ajax} from "rxjs/observable/dom/ajax";
import {LOAD_LOCATION, loadLocationSuccess} from "../actions/location";
import {PATHS} from "../constants/api";

// export const loadNearLocationsEpic = action$ =>
//   action$.ofType(LOAD_NEAR_LOCATIONS)
//     .mergeMap(action =>
//       ajax({url: PATHS.location(action.lat, action.lng), responseType: 'json', crossDomain: true})
//         .map(response => loadNearLocationsSuccess(response))
//     );

export const loadLocationEpic = action$ =>
  action$.ofType(LOAD_LOCATION)
    .mergeMap(action =>
      ajax({url: PATHS.location(action.name, action.lat, action.lng), responseType: 'json', crossDomain: true})
        .map(response => loadLocationSuccess(response))
    );

export const locationEpic = combineEpics(
  // loadNearLocationsEpic,
  loadLocationEpic
);
