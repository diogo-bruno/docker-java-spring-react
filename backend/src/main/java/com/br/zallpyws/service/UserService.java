package com.br.zallpyws.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.zallpyws.model.Project;
import com.br.zallpyws.model.User;
import com.br.zallpyws.security.TokenUtility;
import com.br.zallpyws.util.DAO;
import com.br.zallpyws.util.Utility;
import com.br.zallpyws.vo.UserVO;

@SuppressWarnings("unchecked")
@Service
public class UserService {

  @Autowired DAO dao;
  @Autowired TokenUtility tokenUtility;
  @Autowired ProjectService projectService;
  @Autowired TimeWorkedService timeWorkedService;

  public User createUser(UserVO userVO) {
    User user = new User();
    user.setEmail(userVO.getEmail());
    user.setPassword(Utility.generateHashFromString(userVO.getPassword()));
    user.setName(userVO.getName());
    user.setProfiles(userVO.getProfiles());
    List<Project> projectsUser = new ArrayList<Project>();
    for (Integer idProject : userVO.getIdProjects()) {
      Project projectUser = projectService.getProject(idProject);
      if (projectUser != null)
        projectsUser.add(projectUser);
    }
    user.setProjects(projectsUser);
    dao.save(user);
    return user;
  }

  public List<User> listAllUser() {
    return (List<User>) dao.list("from User");
  }

  public List<User> listAllUsersAndTimeWorked() {
    List<User> users = listAllUser();
    for (User user : users) {
      for (Project project : user.getProjects()) {
        project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByUser(user.getId()));
      }
    }
    return users;
  }

  public User getUser(Integer userId) {
    return (User) dao.single("from User wehere id = ?1", userId);
  }

  public User getProjectsAndTimeWorkedByUser(Integer userId) {
    User user = getUser(userId);
    for (Project project : user.getProjects()) {
      project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByUser(user.getId()));
    }
    return user;
  }

  public UserVO getUserLogin(String email, String password) {
    User user = (User) dao.single("from User wehere email = ?1", email);
    if (user.getPassword().equals(Utility.generateHashFromString(password))) {
      String token = tokenUtility.createTokenByUser(user);
      UserVO userVO = new UserVO();
      userVO.setEmail(user.getEmail());
      userVO.setPassword(user.getPassword());
      userVO.setProfiles(user.getProfiles());
      userVO.setToken(token);
      return userVO;
    }
    return null;
  }

}