import {LOAD_RANK_USERS_SUCCESS} from "../actions/user";

const initState = {
  rank: [],
};

export function user(state = initState, action) {
  switch (action.type) {
    case LOAD_RANK_USERS_SUCCESS:
      return {
        ...state,
        rank: action.payload.response,
      };
    default:
      return state
  }
}
