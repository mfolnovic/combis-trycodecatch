/*
 * action types
 */
export const LOAD_NEAR_LOCATIONS = 'LOAD_NEAR_LOCATIONS';
export const LOAD_MY_LOCATION = 'LOAD_MY_LOCATION';

/*
 * action creators
 */
export function loadNearLocations(status = 'LOADING', lat, lng, payload = {response: []}) {
  return {type: LOAD_NEAR_LOCATIONS, status, lat, lng, payload}
}

export function loadMyLocation(status = 'LOADING', payload = {response: []}) {
  return {type: LOAD_MY_LOCATION, status, payload}
}