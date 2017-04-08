import {combineEpics} from "redux-observable";
import {locationEpic} from './location';
import {userEpic} from './user';

export const rootEpic = combineEpics(
  locationEpic,
  userEpic,
);
