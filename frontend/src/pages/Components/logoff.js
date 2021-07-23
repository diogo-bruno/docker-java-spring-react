import React from 'react';
import { connect } from 'react-redux';
import { logoff } from '../../store/actions';
import PropTypes from 'prop-types';

import { ButtonLogoff } from '../../styles/styles-styled-components';

let Logoff = ({ logoffProps }) => {
  return (
    <div>
      <ButtonLogoff className="button" onClick={logoffProps}>
        Sair
      </ButtonLogoff>
    </div>
  );
};

Logoff.propTypes = {
  logoffProps: PropTypes.func,
};

const mapDispatchToProps = (dispatch) => {
  return {
    logoffProps: () => dispatch(logoff({ userAction: true })),
  };
};

Logoff = connect(null, mapDispatchToProps)(Logoff);

export default Logoff;
