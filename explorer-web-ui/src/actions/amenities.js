/*
 * action types
 */
export const LOAD_NEAR_AMENITIES = 'LOAD_NEAR_AMENITIES';
export const LOAD_NEAR_AMENITIES_SUCCESS = 'LOAD_NEAR_AMENITIES_SUCCESS';

/*
 * action creators
 */
export function loadNearAmenities(lat, lng) {
  return {type: LOAD_NEAR_AMENITIES, lat, lng};
}

export function loadNearAmenitiesSuccess(payload) {
  return {type: LOAD_NEAR_AMENITIES_SUCCESS, payload};
}
