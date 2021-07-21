package com.br.zallpyws.ws;

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

import com.br.zallpyws.model.Project;
import com.br.zallpyws.rule.Profile;
import com.br.zallpyws.service.ProjectService;
import com.br.zallpyws.vo.ProjectVO;

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
  public List<Project> listAllProjectsAndTimeWorked(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("id") Integer id) throws Exception {
    return projectService.listAllProjectsAndTimeWorked();
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/getProject/{id}", method = { RequestMethod.GET })
  public Project getProject(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("id") Integer id) throws Exception {
    return projectService.getProject(id);
  }

}
