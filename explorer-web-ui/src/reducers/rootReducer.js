import {combineReducers} from "redux";
import {amenity} from "./amenityReducers";
import {location} from "./locationReducer";
import {user} from "./userReducer";

export const rootReducer = combineReducers({
  amenity,
  location,
  user,
});
