package com.br.zallpyws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.zallpyws.model.Project;
import com.br.zallpyws.security.TokenUtility;
import com.br.zallpyws.util.DAO;
import com.br.zallpyws.vo.ProjectVO;

@SuppressWarnings("unchecked")
@Service
public class ProjectService {

  @Autowired DAO dao;
  @Autowired TokenUtility tokenUtility;
  @Autowired TimeWorkedService timeWorkedService;

  public Project createProject(ProjectVO projectVO) {
    Project project = new Project();
    project.setDescription(projectVO.getDescription());
    return project;
  }

  public List<Project> listAllProjects() {
    return (List<Project>) dao.list("from Project");
  }

  public List<Project> listAllProjectsAndTimeWorked() {
    List<Project> projects = (List<Project>) dao.list("from Project");
    for (Project project : projects) {
      project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByProject(project.getId()));
    }
    return projects;
  }

  public Project getProject(Integer projectId) {
    return (Project) dao.single("from Project wehere id = ?1", projectId);
  }

}
