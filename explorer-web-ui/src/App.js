import React, {Component} from "react";
import "./App.css";
import {Provider} from "react-redux";
import {createInitStore} from "./store/store";
import NearLocationsContainer from "./containers/NearLocationsContainer";
import MyLocationContainer from "./containers/MyLocationContainer";
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

let store = createInitStore();

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <MuiThemeProvider>
          <MyLocationContainer>
            <div className="App">
              <div className="App-header">
                <h1>Explorer</h1>
              </div>
              <NearLocationsContainer />
            </div>
          </MyLocationContainer>
        </MuiThemeProvider>
      </Provider>
    );
  }
}

export default App;
