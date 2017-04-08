import {combineEpics} from "redux-observable";
import {loadNearLocationsEpic} from './location';

export const rootEpic = combineEpics(
  loadNearLocationsEpic
);
