/* eslint-disable complexity */
import axios from 'axios';

import { logoff } from '../store/actions';

export const configAxios = (storeDispatch) => {
  axios.interceptors.request.use(
    (config) => {
      config.timeout = 180000;
      config.headers.token = window.localStorage.getItem('token');
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  axios.interceptors.response.use(
    (response) => {
      if (!response) {
        storeDispatch(logoff());
      }
      return response;
    },
    (error) => {
      if (error.response && (error.response.data || error.response.status)) {
        const statusCode = error.response.status;
        if (statusCode === 400 && error.response.data) {
          window.alert(error.response.data.message);
        } else if (statusCode === 403) {
          window.alert(
            `Sem permissão - 403\n\nService URL: ${error.response.config.url}`
          );
        } else if (statusCode === 401) {
          window.alert(
            `Token invalido - 401\n\nService URL: ${error.response.config.url}`
          );
          storeDispatch(logoff(true));
        } else {
          window.alert(
            `Algo estranho acontenceu - StatusCode é ${statusCode}\n\n`
          );
        }
      } else if (error.response === 'Error: Network Error') {
        window.alert(
          `Falha\n\nNão foi possível acessar o serviço - ${error.config.url}`
        );
      } else {
        window.alert(
          'Erro ao conectar no service\n\n' +
            `${error.message} - ${error.config.url}`
        );
      }
      return Promise.reject(error);
    }
  );
};
