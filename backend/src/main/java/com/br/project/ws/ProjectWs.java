package com.br.project.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.br.project.model.Project;
import com.br.project.rule.Profile;
import com.br.project.service.ProjectService;
import com.br.project.vo.ProjectVO;

@RestController
public class ProjectWs {

  @Autowired ProjectService projectService;

  @Profile(Profile.ADM)
  @RequestMapping(value = "/createProject", method = { RequestMethod.POST })
  public Project createProject(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody ProjectVO projectVO) throws Exception {
    return projectService.createProject(projectVO);
  }

  @Profile(Profile.ADM)
  @RequestMapping(value = "/listAllProjects", method = { RequestMethod.GET })
  public List<Project> listAllProjects(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("id") Integer id) throws Exception {
    return projectService.listAllProjects();
  }

  @Profile(Profile.ADM)
  @RequestMapping(value = "/listAllProjectsAndTimeWorked", method = { RequestMethod.GET })
  public List<Project> listAllProjectsAndTimeWorked(HttpServletRequest request, @RequestHeader HttpHeaders headers) throws Exception {
    return projectService.listAllProjectsAndTimeWorked();
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/getProject/{projectId}", method = { RequestMethod.GET })
  public Project getProject(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("projectId") Integer projectId) throws Exception {
    return projectService.getProject(projectId);
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/getAllProjectByUserId/{userId}", method = { RequestMethod.GET })
  public List<Project> getAllProjectByUserId(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("userId") Integer userId) throws Exception {
    return projectService.getAllProjectByUserId(userId, false);
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/listAllProjectByUserId/{userId}", method = { RequestMethod.GET })
  public List<Project> listAllProjectByUserId(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("userId") Integer userId) throws Exception {
    return projectService.getAllProjectByUserId(userId, true);
  }

}
