import {combineReducers} from "redux";
import {location} from "./locationReducer";
import {user} from "./userReducer";

export const rootReducer = combineReducers({
  location,
  user,
});
