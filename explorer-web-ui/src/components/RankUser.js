import React, {PropTypes} from "react";
import User from "./User";
import {List} from 'material-ui/List';
import Divider from 'material-ui/Divider';

function RankUser({users}) {
  let items = [];
  users.forEach((user, i) => {
    items.push(<User key={user.id} user={user}/>);
    if (i + 1 < users.length) {
      items.push(<Divider key={i} inset={false}/>);
    }
  });
  return (
    <List>
      {items}
    </List>
  );
}

RankUser.propTypes = {
  users: PropTypes.array.isRequired,
};

export default RankUser;
