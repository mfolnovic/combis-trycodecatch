/*
 * action types
 */
export const LOAD_RANK_USERS = 'LOAD_RANK_USERS';
export const LOAD_RANK_USERS_SUCCESS = 'LOAD_RANK_USERS_SUCCESS';

/*
 * action creators
 */
export function loadRankUsers() {
  return {type: LOAD_RANK_USERS};
}

export function loadRankUsersSuccess(payload) {
  return {type: LOAD_RANK_USERS_SUCCESS, payload};
}
