import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { urlsServices } from '../../configs/urlsConfig';
import TopPage from '../Components/topPage';

let Projects = ({ dataUser }) => {
  window.console.log(dataUser);

  const [allProjects, setAllProjects] = useState([]);
  const [myProjects, setMyProjects] = useState([]);

  const userId = dataUser?.id;
  const isAdm = dataUser?.profiles?.includes('ADM');

  useEffect(() => {
    if (isAdm) {
      axios
        .get(`${urlsServices.zallpyWS}listAllProjectsAndTimeWorked`)
        .then((response) => {
          setAllProjects(response.data);
        });
    }

    if (userId) {
      axios
        .get(`${urlsServices.zallpyWS}getAllProjectByUserId/${userId}`)
        .then((response) => {
          setMyProjects(response.data);
        });
    }
  }, [userId]);

  function timeConvert(time) {
    const hours = time / 60;
    const rhours = Math.floor(hours);
    const minutes = (hours - rhours) * 60;
    const rminutes = Math.round(minutes);
    return rhours + ' hora(s) e ' + rminutes + ' minuto(s).';
  }

  const tableAllProjects = () => {
    return (
      <table>
        <caption>Todos os projetos</caption>
        <thead>
          <tr>
            <th scope="col">Projeto</th>
            <th scope="col">Quantidade trabalhadores</th>
            <th scope="col">Total de horas trabalhadas</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <td colSpan="3">Total de {allProjects.length} projetos</td>
          </tr>
        </tfoot>
        <tbody>
          {allProjects.map((project) => {
            return (
              <tr key={project.id}>
                <th scope="row">{project.description}</th>
                <td>{project.totalWorkers}</td>
                <td>{timeConvert(project.totalMinutesWorked)}</td>
              </tr>
            );
          })}
        </tbody>
      </table>
    );
  };
  const tableMyProjects = () => {
    return (
      <table>
        <caption>Meus projetos</caption>
        <thead>
          <tr>
            <th scope="col">Projeto</th>
            <th scope="col">Horas trabalhadas</th>
            <th scope="col">Opções</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <td colSpan="3">Total de {myProjects.length} projetos</td>
          </tr>
        </tfoot>
        <tbody>
          {myProjects.map((project) => {
            return (
              <tr key={project.id}>
                <th scope="row">{project.description}</th>
                <td>{timeConvert(project.totalMinutesWorked)}</td>
                <td>
                  <button>Excluir</button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    );
  };

  return (
    <div>
      <TopPage />

      <div className="box-display">
        {isAdm ? (
          <div>
            {tableAllProjects()} {tableMyProjects()}
          </div>
        ) : (
          tableMyProjects()
        )}
      </div>
    </div>
  );
};

Projects.propTypes = {
  dataUser: PropTypes.object,
};

const mapStateToProps = (state) => ({
  dataUser: state,
});

Projects = connect(mapStateToProps, null)(Projects);

export default Projects;
