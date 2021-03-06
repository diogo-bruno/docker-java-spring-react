package com.br.project.ws;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.br.project.model.Project;
import com.br.project.model.User;
import com.br.project.service.ProjectService;
import com.br.project.service.TimeWorkedService;
import com.br.project.service.UserService;
import com.br.project.util.DAO;
import com.br.project.util.Public;
import com.br.project.vo.ProjectVO;
import com.br.project.vo.UserVO;
import com.google.common.collect.ImmutableMap;

@RestController
public class TestWs {

  @Autowired TimeWorkedService timeWorkedService;
  @Autowired ProjectService projectService;
  @Autowired UserService userService;
  @Autowired DAO dao;

  @Public
  @RequestMapping(value = "/reset-and-create-data-test", method = { RequestMethod.GET })
  public Object resetAndCreateDataTest(HttpServletRequest request, @RequestHeader HttpHeaders headers) throws Exception {

    // DELETE DATA OLD
    dao.updateNative("delete from TimeWorked");
    dao.updateNative("delete from User_profiles");
    dao.updateNative("delete from User_Project");
    dao.updateNative("delete from Project");
    dao.updateNative("delete from User");

    // PROJETO A
    ProjectVO projectVOA = new ProjectVO();
    projectVOA.setDescription("Projeto Cliente A");

    Project newProjectA = projectService.createProject(projectVOA);

    // PROJETO B
    ProjectVO projectVOB = new ProjectVO();
    projectVOB.setDescription("Projeto Cliente B");

    Project newProjectB = projectService.createProject(projectVOB);

    //ADD USER ADM
    UserVO userADM = new UserVO();
    userADM.setEmail("user.adm@gmail.com");
    userADM.setPassword("admin");
    userADM.setName("Diogo Bruno ADM");

    List<String> profilesADM = new ArrayList<String>();
    profilesADM.add("ADM");
    profilesADM.add("PROGRAMADOR");
    userADM.setProfiles(profilesADM);

    List<Integer> idProjects = new ArrayList<Integer>();
    idProjects.add(newProjectA.getId());
    idProjects.add(newProjectB.getId());
    userADM.setIdProjects(idProjects);

    User newUserADM = userService.createUser(userADM);

    //ADD USER PROGRAMADOR 1 
    UserVO userPGR1 = new UserVO();
    userPGR1.setEmail("user.programador1@gmail.com");
    userPGR1.setPassword("programador1");
    userPGR1.setName("Programador 1");

    List<String> profilesPGR1 = new ArrayList<String>();
    profilesPGR1.add("PROGRAMADOR");
    userPGR1.setProfiles(profilesPGR1);

    List<Integer> idProjectsPGR1 = new ArrayList<Integer>();
    idProjectsPGR1.add(newProjectA.getId());
    userPGR1.setIdProjects(idProjectsPGR1);

    User newUserPGR1 = userService.createUser(userPGR1);

    //ADD USER PROGRAMADOR 2 
    UserVO userPGR2 = new UserVO();
    userPGR2.setEmail("user.programador2@gmail.com");
    userPGR2.setPassword("programador2");
    userPGR2.setName("Programador 2");

    List<String> profilesPGR2 = new ArrayList<String>();
    profilesPGR2.add("PROGRAMADOR");
    userPGR2.setProfiles(profilesPGR2);

    List<Integer> idProjectsPGR2 = new ArrayList<Integer>();
    idProjectsPGR2.add(newProjectA.getId());
    idProjectsPGR2.add(newProjectB.getId());
    userPGR2.setIdProjects(idProjectsPGR2);

    User newUserPGR2 = userService.createUser(userPGR2);

    ImmutableMap<String, Object> immutableMap = ImmutableMap.of("projetoA", newProjectA, "projetoB", newProjectB, "ADM", newUserADM, "PROGRAMADOR_1", newUserPGR1, "PROGRAMADOR_2", newUserPGR2);

    return immutableMap;

  }

}
