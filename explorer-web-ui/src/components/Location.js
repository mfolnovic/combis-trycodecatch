import React, {PropTypes} from "react";
import {Icon} from "react-fa";

function Location({location}) {
  return (
    <div>
      <h1>{location.name}</h1>
      <a href={ "https://trycodecatch-explorer.slack.com/messages/"+ location.channel.slackId +"/" }><Icon name="slack"/>{location.channel.name}</a>
    </div>
  );
}

Location.propTypes = {
  location: PropTypes.object.isRequired
};

export default Location;