/*
 * action types
 */
// export const LOAD_NEAR_LOCATIONS = 'LOAD_NEAR_LOCATIONS';
// export const LOAD_NEAR_LOCATIONS_SUCCESS = 'LOAD_NEAR_LOCATIONS_SUCCESS';
export const LOAD_LOCATION = 'LOAD_LOCATION';
export const LOAD_LOCATION_SUCCESS = 'LOAD_LOCATION_SUCCESS';
export const LOAD_MY_LOCATION = 'LOAD_MY_LOCATION';
export const SET_NEW_CENTER = 'SET_NEW_CENTER';

/*
 * action creators
 */
export function loadLocation(name, lat, lng) {
  return {type: LOAD_LOCATION, name, lat, lng};
}

export function loadLocationSuccess(payload) {
  console.debug(payload);
  return {type: LOAD_LOCATION_SUCCESS, payload};
}

export function setNewCenter(payload) {
  return {type: SET_NEW_CENTER, payload}
}

export function loadMyLocation(payload) {
  return {type: LOAD_MY_LOCATION, payload}
}
