import React, { useState } from 'react';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import { login } from '../../store/actions';
import PropTypes from 'prop-types';
import logoImg from '../../assets/logo.svg';
import launchPageImg from '../../assets/launch-page.png';

import './styles.css';

let Logon = ({ loginProps, stateToProps }) => {
  const [email, setEmail] = useState('user.adm@gmail.com');
  const [password, setPassword] = useState('admin');

  if (stateToProps.isAuthenticated) {
    window.localStorage.setItem('token', stateToProps.token);
  }

  function handleLogin(event) {
    event.preventDefault();
    loginProps({ email, password });
  }

  return stateToProps.isAuthenticated ? (
    <Redirect to="/home" />
  ) : (
    <div className="logon-container">
      <section className="form">
        <img src={logoImg} alt="Logo" />

        <form onSubmit={handleLogin} autoComplete="new-password">
          <input
            type="email"
            placeholder="Email"
            autoComplete="new-password"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
          />

          <input
            type="password"
            placeholder="Senha"
            autoComplete="new-password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
          />

          <button className="button" type="submit">
            Entrar
          </button>
        </form>
      </section>

      <img src={launchPageImg} alt="LaunchPage" />
    </div>
  );
};

Logon.propTypes = {
  loginProps: PropTypes.func,
  stateToProps: PropTypes.object,
};

const mapStateToProps = (state) => ({
  stateToProps: state,
});

const mapDispatchToProps = (dispatch) => {
  return {
    loginProps: (dataLogin) => dispatch(login(dataLogin)),
  };
};

Logon = connect(mapStateToProps, mapDispatchToProps)(Logon);

export default Logon;
