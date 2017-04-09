import {LOAD_LOCATION_SUCCESS, LOAD_MY_LOCATION, SET_NEW_CENTER} from "../actions/location";

const initState = {
  loaded: null,
  near: [],
  my: null,
};

export function location(state = initState, action) {
  console.debug(action);
  switch (action.type) {
    case LOAD_LOCATION_SUCCESS:
      return {
        ...state,
        loaded: action.payload.response,
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
