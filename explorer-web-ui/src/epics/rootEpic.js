import {combineEpics} from "redux-observable";
import {locationEpic} from './location';
import {amenityEpic} from './amenity';
import {userEpic} from './user';

export const rootEpic = combineEpics(
  amenityEpic,
  locationEpic,
  userEpic,
);
