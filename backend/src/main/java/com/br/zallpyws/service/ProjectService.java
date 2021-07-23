package com.br.zallpyws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.zallpyws.model.Project;
import com.br.zallpyws.model.User;
import com.br.zallpyws.security.TokenUtility;
import com.br.zallpyws.util.DAO;
import com.br.zallpyws.vo.ProjectVO;

@SuppressWarnings("unchecked")
@Service
public class ProjectService {

  @Autowired DAO dao;
  @Autowired TokenUtility tokenUtility;
  @Autowired TimeWorkedService timeWorkedService;
  @Autowired UserService userService;

  public Project createProject(ProjectVO projectVO) {
    Project project = new Project();
    project.setDescription(projectVO.getDescription());
    dao.save(project);
    return project;
  }

  public List<Project> listAllProjects() {
    return (List<Project>) dao.list("from Project");
  }

  public List<Project> listAllProjectsAndTimeWorked() {
    List<Project> projects = (List<Project>) dao.list("from Project");

    for (Project project : projects) {
      project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByProject(project.getId()));
      project.setTotalWorkers(timeWorkedService.getCountUsersByProject(project.getId()));
    }

    return projects;
  }

  public Project getProject(Integer projectId) {
    return (Project) dao.single("from Project p where p.id = ?1", projectId);
  }

  public List<Project> getAllProjectByUserId(Integer userId, Boolean ignoreMinutesWorked) {

    User user = userService.getUser(userId);

    List<Integer> idProjects = (List<Integer>) dao.listNative("SELECT projects_id as id FROM user_project where User_id = ?1", userId);

    List<Project> projects = (List<Project>) dao.list("from Project p where p.id in (?1)", idProjects);

    if (!ignoreMinutesWorked)
      for (Project project : projects) {
        project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByProjectAndUser(project.getId(), user.getId()));
      }

    return projects;
  }

}
