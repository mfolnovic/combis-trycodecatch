import React, {PropTypes} from "react";
import {Icon} from "react-fa";
import {ListItem} from 'material-ui/List';

function Location({location}) {
  return (
    <ListItem
      primaryText={location.name}
      secondaryText={location.channel.name}
      rightIcon={<Icon name="slack"/>}
      onTouchTap={() => window.location.href = "https://trycodecatch-explorer.slack.com/messages/" + location.channel.slackId +"/"}
      />
  );
}

Location.propTypes = {
  location: PropTypes.object.isRequired
};

export default Location;