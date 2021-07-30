import React, { useState } from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';

import { urlsServices } from './configs/urlsConfig';
import PropTypes from 'prop-types';

import { setDataUserLogin } from './store/actions';

import Logon from './pages/Logon';
import Home from './pages/Home';
import Laoading from './pages/Components/loading';
import Projects from './pages/Projects';
import TimeWorked from './pages/TimeWorked';

let Routes = ({ isAuthenticated, setDataUserLoginProps }) => {
  const [token] = useState(window.localStorage.getItem('token'));
  const [tokenValid, setTokenValid] = useState(false);

  if (!isAuthenticated && token) {
    axios
      .get(`${urlsServices.projectWS}validate?token=${token}`)
      .then((response) => {
        if (response.data) {
          setTokenValid(true);
          setDataUserLoginProps(response.data);
        } else {
          window.localStorage.setItem('token', '');
          setTokenValid(false);
          window.location.href = '/';
        }
      });
  }

  const checkAuthenticated = () => {
    return isAuthenticated === true ? isAuthenticated : tokenValid;
  };

  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" exact component={Logon} />
        <PrivateRoute
          path="/home"
          tokenExist={token}
          isAuthenticated={checkAuthenticated()}
          component={Home}
        />
        <PrivateRoute
          path="/projects"
          tokenExist={token}
          isAuthenticated={checkAuthenticated()}
          component={Projects}
        />
        <PrivateRoute
          path="/time-worked"
          tokenExist={token}
          isAuthenticated={checkAuthenticated()}
          component={TimeWorked}
        />
      </Switch>
    </BrowserRouter>
  );
};

Routes.propTypes = {
  isAuthenticated: PropTypes.bool,
  setDataUserLoginProps: PropTypes.func,
};

const PrivateRoute = ({
  component: Component,
  isAuthenticated,
  tokenExist,
  ...rest
}) => (
  <Route
    {...rest}
    render={(props) => {
      if (isAuthenticated) {
        return <Component {...props} />;
      }

      if (tokenExist) {
        return <Laoading />;
      }

      return (
        <Redirect
          to={{
            pathname: '/',
          }}
        />
      );
    }}
  />
);

PrivateRoute.propTypes = {
  component: PropTypes.any,
  isAuthenticated: PropTypes.bool,
  tokenExist: PropTypes.any,
};

const mapStateToProps = (state) => ({
  isAuthenticated: state.isAuthenticated,
});

const mapDispatchToProps = (dispatch) => {
  return {
    setDataUserLoginProps: (dataUser) => dispatch(setDataUserLogin(dataUser)),
  };
};

Routes = connect(mapStateToProps, mapDispatchToProps)(Routes);

export default Routes;
