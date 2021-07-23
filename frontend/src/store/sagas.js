import axios from 'axios';
import { put, takeLatest, all } from 'redux-saga/effects';
import { urlsServices } from '../configs/urlsConfig';

function* fetchLogin(action) {
  const axiosResponse = yield axios
    .post(`${urlsServices.zallpyWS}login`, action.dataLogin)
    .then(
      (response) => {
        if (response.data && response.data.token) {
          return { ...response.data, isAuthenticated: true };
        }
        return { ...response.data, isAuthenticated: false };
      },
      (error) => {
        return { ...error.message, isAuthenticated: false };
      }
    );

  yield put({
    type: 'DATA_USER_LOGIN',
    ...axiosResponse,
  });
}

function* fetchLogoff(action) {
  yield axios
    .get(
      `${urlsServices.zallpyWS}logoff?token=${window.localStorage.getItem(
        'item'
      )}`
    )
    .then(
      (response) => {
        if (action.userAction) {
          window.localStorage.setItem('token', '');
          window.location.href = '/';
        }
        return { isAuthenticated: false };
      },
      (error) => {
        return { ...error.message, isAuthenticated: false };
      }
    );
}

function* setDataUser(action) {
  yield put({
    type: 'DATA_USER_LOGIN',
    ...action.dataUser,
  });
}

function* actionSetDataUser() {
  yield takeLatest('SET_DATA_USER_LOGIN', setDataUser);
}

function* actionLogin() {
  yield takeLatest('LOGIN', fetchLogin);
}

function* actionLogoff() {
  yield takeLatest('LOGOFF', fetchLogoff);
}

export default function* rootSaga() {
  yield all([actionLogin(), actionLogoff(), actionSetDataUser()]);
}
