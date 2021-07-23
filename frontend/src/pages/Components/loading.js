import React from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import img from '../../assets/loading.gif';

import {
  ContainerCenterFixed,
  TextStyle,
} from '../../styles/styles-styled-components';

let Laoading = ({ loading }) =>
  loading ? (
    <ContainerCenterFixed>
      <img style={{ borderRadius: '50%', opacity: 0.5 }} src={img} />
      <TextStyle>Aguarde...</TextStyle>
    </ContainerCenterFixed>
  ) : null;

Laoading.propTypes = {
  loading: PropTypes.bool,
};

const mapStateToProps = (state) => ({
  loading: state.loading,
});

Laoading = connect(mapStateToProps, null)(Laoading);

export default Laoading;
