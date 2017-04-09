import React, {PropTypes} from "react";
import {ListItem} from 'material-ui/List';

function User({rank, user}) {
  let rightIcon = null;
  if (rank < 3) {
    rightIcon = <i className={'fa fa-trophy trophy-' + rank} aria-hidden="true"></i>;
  }

  return (
    <ListItem
      primaryText={user.username}
      secondaryText={'Score: ' + user.total_score}
      rightIcon={rightIcon}
    />
  );
}

User.propTypes = {
  user: PropTypes.object.isRequired
};

export default User;
