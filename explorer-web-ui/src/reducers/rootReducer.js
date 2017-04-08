import {combineReducers} from "redux";
import {location} from "./locationReducer";

export const rootReducer = combineReducers({
  location,
});
