import {combineEpics} from "redux-observable";
import {ajax} from "rxjs/observable/dom/ajax";
import {LOAD_RANK_USERS, loadRankUsersSuccess} from "../actions/user";
import {PATHS} from "../constants/api";

export const loadRankUsersEpic = action$ =>
  action$.ofType(LOAD_RANK_USERS)
    .mergeMap(action =>
      ajax({url: PATHS.rankUsers(), responseType: 'json', crossDomain: true})
        .map(response => loadRankUsersSuccess(response))
    );

export const userEpic = combineEpics(
  loadRankUsersEpic,
);
