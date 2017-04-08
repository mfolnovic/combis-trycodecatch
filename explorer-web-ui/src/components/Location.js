import React, {PropTypes} from "react";
import FontAwesome from "react-fontawesome";

function Location({location}) {
  return (
    <div>
      <h1>{location.name}</h1>
      <a href={ "https://trycodecatch-explorer.slack.com/messages/"+ location.channel.slackId +"/" }><FontAwesome name="slack"/>{location.channel.name}</a>
    </div>
  );
}

Location.propTypes = {
  location: PropTypes.object.isRequired
};

export default Location;