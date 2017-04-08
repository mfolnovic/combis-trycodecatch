import {LOAD_NEAR_LOCATIONS_SUCCESS, LOAD_MY_LOCATION, SET_NEW_CENTER} from "../actions/location";

const initState = {
  near: [],
  my: null,
};

export function location(state = initState, action) {
  console.debug(action);
  switch (action.type) {
    case LOAD_NEAR_LOCATIONS_SUCCESS:
      return {
        ...state,
        near: action.payload.response,
      };
    case LOAD_MY_LOCATION:
      return {
        ...state,
        my: action.payload,
        center: action.payload
      };
    case SET_NEW_CENTER:
      return {
        ...state,
        center: action.payload
      };
    default:
      return state
  }
}
