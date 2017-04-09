import {LOAD_NEAR_AMENITIES_SUCCESS} from "../actions/amenities";

const initState = {
  near: [],
};

export function amenity(state = initState, action) {
  switch (action.type) {
    case LOAD_NEAR_AMENITIES_SUCCESS:
      return {
        ...state,
        near: action.payload.response,
      };
    default:
      return state
  }
}
