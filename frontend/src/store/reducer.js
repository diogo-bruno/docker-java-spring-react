const reducer = (state = {}, action) => {
  switch (action.type) {
    case 'LOGIN':
      return { ...state };
    case 'DATA_USER_LOGIN':
      return { ...state, ...action };
    default:
      return state;
  }
};

export default reducer;
