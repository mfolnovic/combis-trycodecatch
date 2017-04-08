import React from 'react'

import Map, {GoogleApiWrapper} from 'google-maps-react'

const MapContainer = React.createClass({
  onMapMoved: function(props, map) {
    const center = map.center;
  },

  onMapClicked: function(props) {
    if (this.state.showingInfoWindow) {
      this.setState({
        showingInfoWindow: false,
        activeMarker: null
      })
    }
  },

  render: function() {
    if (!this.props.loaded) {
      return <div>Loading...</div>
    }

    return (
      <Map google={this.props.google}
           className={'map'}
           style={{}}
           zoom={14}
           containerStyle={{width: '500px', height: '500px', position: 'relative'}}
           centerAroundCurrentLocation={true}
           onClick={this.onMapClicked}
           onDragend={this.onMapMoved} />
    )
  }
});

export default GoogleApiWrapper({
  apiKey: "AIzaSyAyesbQMyKVVbBgKVi2g6VX7mop2z96jBo",
  libraries: ['places','visualization']
})(MapContainer);