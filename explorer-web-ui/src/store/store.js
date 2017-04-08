import {createEpicMiddleware} from "redux-observable";
import {applyMiddleware, createStore} from "redux";
import {rootReducer} from "../reducers/rootReducer";
import {rootEpic} from "../epics/rootEpic";

export function createInitStore() {
  const epicMiddleware = createEpicMiddleware(rootEpic);

  return createStore(
    rootReducer,
    applyMiddleware(epicMiddleware)
  );
}
