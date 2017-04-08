import React, {Component} from "react";
import {connect} from "react-redux";
import {loadRankUsers} from "../actions/user";
import RankUser from "../components/RankUser";

class RankUserContainer extends Component {
  componentDidMount() {
    this.props.dispatch(loadRankUsers());
  }

  render() {
    return <RankUser users={this.props.users} />;
  }
}

const mapStateToProps = (state, ownProps) => {
  return {
    users: state.user.rank,
  }
};

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    dispatch: dispatch,
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RankUserContainer);
