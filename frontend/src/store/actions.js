export const setDataUserLogin = (dataUser) => ({
  type: 'SET_DATA_USER_LOGIN',
  dataUser,
});

export const login = (dataLogin) => ({
  type: 'LOGIN',
  dataLogin,
});

export const logoff = (userAction) => ({
  type: 'LOGOFF',
  userAction,
});
