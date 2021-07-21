
package com.br.zallpyws.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.zallpyws.model.Project;
import com.br.zallpyws.model.TimeWorked;
import com.br.zallpyws.model.User;
import com.br.zallpyws.util.DAO;
import com.br.zallpyws.vo.TimeWorkedVO;

@SuppressWarnings("unchecked")
@Service
public class TimeWorkedService {

  @Autowired DAO dao;

  @Autowired ProjectService projectService;
  @Autowired UserService userService;

  public TimeWorked createTimeWorked(TimeWorkedVO timeWorkedVO) {
    TimeWorked timeWorked = new TimeWorked();

    timeWorked.setStartWork(timeWorkedVO.getStartWork());
    timeWorked.setEndWork(timeWorkedVO.getEndWork());

    Project projectWorked = projectService.getProject(timeWorkedVO.getProjectId());
    timeWorked.setProject(projectWorked);

    User user = userService.getUser(timeWorkedVO.getUserId());
    timeWorked.setUser(user);

    dao.save(timeWorked);
    return timeWorked;
  }

  public long getAllMinutesWorkedByUser(Integer idUser) {
    long totaMinutes = 0;
    List<TimeWorked> timeWorkedUser = (List<TimeWorked>) dao.list("from TimeWorked where user.id = ?1", idUser);
    for (TimeWorked timeWorked : timeWorkedUser) {
      long diff = timeWorked.getStartWork().getTime() - timeWorked.getEndWork().getTime();
      totaMinutes += TimeUnit.MILLISECONDS.toMinutes(diff);
    }
    return totaMinutes;
  }

  public long getAllMinutesWorkedByProject(Integer projectId) {
    long totaMinutes = 0;
    List<TimeWorked> timeWorkedUser = (List<TimeWorked>) dao.list("from TimeWorked where project.id = ?1", projectId);
    for (TimeWorked timeWorked : timeWorkedUser) {
      long diff = timeWorked.getStartWork().getTime() - timeWorked.getEndWork().getTime();
      totaMinutes += TimeUnit.MILLISECONDS.toMinutes(diff);
    }
    return totaMinutes;
  }

}
