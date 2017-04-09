import React, {PropTypes} from "react";
import User from "./User";
import {List} from 'material-ui/List';
import Divider from 'material-ui/Divider';

function RankUser({users}) {
  let items = [];
  users.forEach((user, i) => {
    items.push(<User key={user.id + 'a'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'b'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'c'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'd'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'e'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'f'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'g'} rank={i} user={user}/>);
    items.push(<User key={user.id + 'h'} rank={i} user={user}/>);
    if (i + 1 < users.length) {
      items.push(<Divider key={'div' + i} inset={false}/>);
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
