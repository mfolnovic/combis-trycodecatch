import {combineEpics} from "redux-observable";
import {locationEpic} from './location';

export const rootEpic = combineEpics(
  locationEpic,
);
