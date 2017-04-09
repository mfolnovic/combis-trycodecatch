import React, {Component} from "react";
import "./App.css";
import {Provider} from "react-redux";
import {createInitStore} from "./store/store";
import MyLocationContainer from "./containers/MyLocationContainer";
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import Layout from "./Layout";

let store = createInitStore();

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <MuiThemeProvider>
          <MyLocationContainer>
            <Layout />
          </MyLocationContainer>
        </MuiThemeProvider>
      </Provider>
    );
  }
}

export default App;
