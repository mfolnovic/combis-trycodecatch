/*
 * action types
 */
export const LOAD_NEAR_LOCATIONS = 'LOAD_NEAR_LOCATIONS';
export const LOAD_NEAR_LOCATIONS_SUCCESS = 'LOAD_NEAR_LOCATIONS_SUCCESS';
export const LOAD_MY_LOCATION = 'LOAD_MY_LOCATION';
export const SET_NEW_CENTER = 'SET_NEW_CENTER';

/*
 * action creators
 */
export function loadNearLocations(lat, lng) {
  return {type: LOAD_NEAR_LOCATIONS, lat, lng};
}

export function loadNearLocationsSuccess(payload) {
  return {type: LOAD_NEAR_LOCATIONS_SUCCESS, payload};
}

export function setNewCenter(payload) {
  return {type: SET_NEW_CENTER, payload}
}

export function loadMyLocation(payload) {
  return {type: LOAD_MY_LOCATION, payload}
}
