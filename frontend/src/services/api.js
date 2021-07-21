import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8010',
});

export default api;
