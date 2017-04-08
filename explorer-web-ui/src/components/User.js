import React, {PropTypes} from "react";
import {Icon} from "react-fa";
import {ListItem} from 'material-ui/List';

function User({user}) {
  return (
    <ListItem
      primaryText={user.username}
      secondaryText={'Score: ' + user.total_score}
    />
  );
}

User.propTypes = {
  user: PropTypes.object.isRequired
};

export default User;
