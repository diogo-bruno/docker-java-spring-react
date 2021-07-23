import React, { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { urlsServices } from '../../configs/urlsConfig';
import TopPage from '../Components/topPage';

let TimeWorked = ({ dataUser }) => {
  window.console.log(dataUser);

  const history = useHistory();

  const [timeStart, setTimeStart] = useState(
    new Date(new Date().setMinutes(new Date().getMinutes() - 60))
  );
  const [timeEnd, setTimeEnd] = useState(new Date());
  const [projectId, setProjectId] = useState();

  const [myProjects, setMyProjects] = useState([]);

  const userId = dataUser?.id;

  useEffect(() => {
    if (userId) {
      axios
        .get(`${urlsServices.zallpyWS}listAllProjectByUserId/${userId}`)
        .then((response) => {
          setMyProjects(response.data);
        });
    }
  }, [userId]);

  function handleTimeWorked(event) {
    event.preventDefault();

    const obj = {
      startWork: timeStart,
      endWork: timeEnd,
      projectId: projectId,
      userId: userId,
    };

    if (!obj.startWork || !obj.endWork || !obj.projectId || !obj.userId) {
      window.alert('É preciso informar todos os campos...');
      return;
    }

    window.console.log(obj);

    axios.post(`${urlsServices.zallpyWS}createTimeWorked`, obj).then(
      (response) => {
        window.console.log(response);
        if (response.data.id) {
          alert('Horas adicionadas com sucesso');
          history.push('/projects');
        }
      },
      (error) => {
        window.console.log(error);
      }
    );
  }

  function isValidDate(date) {
    return date instanceof Date && !isNaN(date);
  }

  function isNumeric(num) {
    return !isNaN(num);
  }

  function splitLastOccurrence(text, splited) {
    return text.substr(0, text.lastIndexOf(splited));
  }

  function formatValueDate(timestamp) {
    if (isValidDate(timestamp)) {
      timestamp = new Date(timestamp).getTime();
    }
    if (timestamp) {
      if (isNumeric(timestamp)) timestamp = parseInt(timestamp);
      return (
        splitLastOccurrence(new Date(timestamp).toJSON(), 'T') +
        'T' +
        splitLastOccurrence(new Date(timestamp).toLocaleTimeString(), ':')
      );
    }
  }

  return (
    <div>
      <TopPage />

      <div className="box-display">
        <section className="form">
          <form onSubmit={handleTimeWorked} autoComplete="new-password">
            <h1 style={{ marginBottom: 20 }}>Adicione horas a um projeto</h1>

            <label>Projeto</label>
            <select
              value={projectId}
              onChange={(event) =>
                setProjectId(
                  event.target.value ? parseInt(event.target.value) : undefined
                )
              }
            >
              <option value="">...</option>
              {myProjects.map((project) => (
                <option key={project.id} value={project.id}>
                  {project.description}
                </option>
              ))}
            </select>

            <label>Ínicio trabalho</label>
            <input
              type="datetime-local"
              placeholder="Data Ínicio"
              value={formatValueDate(timeStart)}
              onChange={(event) => setTimeStart(event.target.value)}
            />

            <label>Fim trabalho</label>
            <input
              type="datetime-local"
              placeholder="Data Fim"
              value={formatValueDate(timeEnd)}
              onChange={(event) => setTimeEnd(event.target.value)}
            />

            <button className="button" type="submit">
              Adicionar
            </button>
          </form>
        </section>
      </div>
    </div>
  );
};

TimeWorked.propTypes = {
  dataUser: PropTypes.object,
};

const mapStateToProps = (state) => ({
  dataUser: state,
});

TimeWorked = connect(mapStateToProps, null)(TimeWorked);

export default TimeWorked;
