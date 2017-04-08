import React, {Component} from "react";
import "./App.css";
import {Provider} from "react-redux";
import {createInitStore} from "./store/store";
import MapContainer from "./components/MapContainter";


let store = createInitStore();

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <div className="App">
          <div className="App-header">
            <h2>Explorer</h2>
          </div>
          <MapContainer/>
        </div>
      </Provider>
    );
  }
}

export default App;
