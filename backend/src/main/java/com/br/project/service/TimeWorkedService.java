
package com.br.project.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.project.model.Project;
import com.br.project.model.TimeWorked;
import com.br.project.model.User;
import com.br.project.util.CustomException;
import com.br.project.util.DAO;
import com.br.project.vo.TimeWorkedVO;

@SuppressWarnings("unchecked")
@Service
public class TimeWorkedService {

  @Autowired DAO dao;

  @Autowired ProjectService projectService;
  @Autowired UserService userService;

  private boolean dateConflict(Date startDate, Date newStar, Date endDate, Date newEnd) {
    if (newStar.compareTo(endDate) <= 0 && newEnd.compareTo(startDate) >= 0) {
      return true;
    }
    return false;
  }

  public TimeWorked createTimeWorked(TimeWorkedVO timeWorkedVO) {

    if (timeWorkedVO.getStartWork() == null) {
      throw new CustomException("Informe a data de ínicio");
    }

    if (timeWorkedVO.getEndWork() == null) {
      throw new CustomException("Informe a data de fim");
    }

    if (timeWorkedVO.getProjectId() == null) {
      throw new CustomException("Informe a data de ínicio");
    }

    if (timeWorkedVO.getStartWork().after(timeWorkedVO.getEndWork())) {
      throw new CustomException("A data de ínicio não pode ser maior que a data final");
    }

    List<Object[]> startDateInProject = (List<Object[]>) dao.listNative("SELECT startWork,endWork FROM timeworked where project_id = ?1 and user_id = ?2", timeWorkedVO.getProjectId(),
        timeWorkedVO.getUserId());

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd H:m:s");

    try {
      if (startDateInProject != null && !startDateInProject.isEmpty())
        for (Object[] startEndDate : startDateInProject) {

          Date start = formatDate.parse(startEndDate[0].toString());
          Date end = formatDate.parse(startEndDate[1].toString());

          if (dateConflict(start, timeWorkedVO.getStartWork(), end, timeWorkedVO.getEndWork())) {
            throw new CustomException("Existe um conflito de datas trabalhadas para este projeto!");
          }
        }

    } catch (Exception e) {
      throw new CustomException(e.getMessage());
    }

    TimeWorked timeWorked = new TimeWorked();
    timeWorked.setStartWork(timeWorkedVO.getStartWork());
    timeWorked.setEndWork(timeWorkedVO.getEndWork());
    Project projectWorked = new Project();
    projectWorked.setId(timeWorkedVO.getProjectId());
    timeWorked.setProject(projectWorked);
    User user = new User();
    user.setId(timeWorkedVO.getUserId());
    timeWorked.setUser(user);
    dao.save(timeWorked);
    return timeWorked;
  }

  public long getAllMinutesWorkedByUser(Integer idUser) {
    long totaMinutes = 0;
    List<TimeWorked> timeWorkedUser = (List<TimeWorked>) dao.list("from TimeWorked where user.id = ?1", idUser);
    for (TimeWorked timeWorked : timeWorkedUser) {
      long diff = timeWorked.getEndWork().getTime() - timeWorked.getStartWork().getTime();
      totaMinutes += TimeUnit.MILLISECONDS.toMinutes(diff);
    }
    return totaMinutes;
  }

  public long getAllMinutesWorkedByProject(Integer projectId) {
    long totaMinutes = 0;
    List<TimeWorked> timeWorkedUser = (List<TimeWorked>) dao.list("from TimeWorked where project.id = ?1", projectId);
    for (TimeWorked timeWorked : timeWorkedUser) {
      long diff = timeWorked.getEndWork().getTime() - timeWorked.getStartWork().getTime();
      totaMinutes += TimeUnit.MILLISECONDS.toMinutes(diff);
    }
    return totaMinutes;
  }

  public Integer getCountUsersByProject(Integer projectId) {
    BigInteger totalUsers = (BigInteger) dao.singleNative("SELECT count(*) as total FROM user_project where projects_id = ?1", projectId);
    return totalUsers.intValue();
  }

  public long getAllMinutesWorkedByProjectAndUser(Integer projectId, Integer userId) {
    long totaMinutes = 0;
    List<TimeWorked> timeWorkedUser = (List<TimeWorked>) dao.list("from TimeWorked where project.id = ?1 and user.id = ?2", projectId, userId);
    for (TimeWorked timeWorked : timeWorkedUser) {
      long diff = timeWorked.getEndWork().getTime() - timeWorked.getStartWork().getTime();
      totaMinutes += TimeUnit.MILLISECONDS.toMinutes(diff);
    }
    return totaMinutes;
  }

}
