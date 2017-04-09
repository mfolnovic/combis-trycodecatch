import {combineEpics} from "redux-observable";
import {ajax} from "rxjs/observable/dom/ajax";
import {LOAD_NEAR_AMENITIES, loadNearAmenitiesSuccess} from "../actions/amenities";
import {PATHS} from "../constants/api";

export const loadNearAmenitiesEpic = action$ =>
  action$.ofType(LOAD_NEAR_AMENITIES)
    .mergeMap(action =>
      ajax({url: PATHS.amenities(action.lat, action.lng), responseType: 'json', crossDomain: true})
        .map(response => loadNearAmenitiesSuccess(response))
    );

export const amenityEpic = combineEpics(
  loadNearAmenitiesEpic,
);
