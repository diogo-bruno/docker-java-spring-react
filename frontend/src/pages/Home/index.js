import React from 'react';
import TopPage from '../Components/topPage';
import launchPageImg from '../../assets/launch-page.png';

const Home = () => (
  <div>
    <TopPage />

    <div className="center-box-home">
      <h1>Controle de horas Trabalhadas</h1>
      <img src={launchPageImg} alt="LaunchPage" />
    </div>
  </div>
);

export default Home;
