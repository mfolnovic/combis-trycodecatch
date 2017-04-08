import React, {Component} from "react";
import "./App.css";
import {Provider} from "react-redux";
import {createInitStore} from "./store/store";
import NearLocationsContainer from "./containers/NearLocationsContainer";
import MyLocationContainer from "./containers/MyLocationContainer";


let store = createInitStore();

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <MyLocationContainer>
          <div className="App">
            <div className="App-header">
              <h2>Explorer</h2>
            </div>
            {/*<MapContainer/>*/}
            <NearLocationsContainer />
          </div>
        </MyLocationContainer>
      </Provider>
    );
  }
}

export default App;
