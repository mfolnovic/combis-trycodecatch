import React, {PropTypes} from "react";
import {Icon} from "react-fa";
import {ListItem} from 'material-ui/List';

function Location({location}) {
  return (
    <ListItem
      primaryText={location.name}
      secondaryText={location.channel.name}
      rightIcon={<Icon name="slack"/>}
      onTouchTap={() => window.open("https://trycodecatch-explorer.slack.com/messages/" + location.channel.slack_id +"/")}
      />
  );
}

Location.propTypes = {
  location: PropTypes.object.isRequired
};

export default Location;
