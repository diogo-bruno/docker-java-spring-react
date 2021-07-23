import React from 'react';
import { Link, useHistory } from 'react-router-dom';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';

import Loading from './loading';
import Logoff from './logoff';

import logoImg from '../../assets/logo.svg';

import './topPage.css';

let TopPage = ({ dataUser }) => {
  // window.console.log(dataUser);

  const history = useHistory();

  return (
    <div className="profile-container">
      <Loading />

      <header>
        <img
          style={{ cursor: 'pointer' }}
          onClick={() => history.push('/home')}
          src={logoImg}
          alt="Logo"
        />
        <span>
          Olá <b>{dataUser && dataUser.name}</b>
          <br />
          <small className="label-email">{dataUser.email}</small>
        </span>

        <div>
          <Link to="/projects">Projetos</Link>
          &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
          <Link to="/time-worked">Adicionar horas</Link>
        </div>

        <Logoff />
      </header>
    </div>
  );
};

TopPage.propTypes = {
  dataUser: PropTypes.object,
};

const mapStateToProps = (state) => ({
  dataUser: state,
});

TopPage = connect(mapStateToProps, null)(TopPage);

export default TopPage;
