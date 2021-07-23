package com.br.zallpyws.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
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
    user.setPassword(Utility.generateBCryptHash(userVO.getPassword()));
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
    return (User) dao.single("from User u where u.id = ?1", userId);
  }

  public User getProjectsAndTimeWorkedByUser(Integer userId) {
    User user = getUser(userId);
    for (Project project : user.getProjects()) {
      project.setTotalMinutesWorked(timeWorkedService.getAllMinutesWorkedByUser(user.getId()));
    }
    return user;
  }

  public UserVO getUserLogin(String email, String password) {
    User user = (User) dao.single("from User u where u.email = ?1", email);
    if (user != null && Utility.checkBCrypt(password, user.getPassword())) {
      UserVO userVO = new UserVO();
      userVO.setEmail(user.getEmail());
      userVO.setName(user.getName());
      userVO.setPassword(user.getPassword());
      userVO.setProfiles(user.getProfiles());
      userVO.setId(user.getId());
      String token = tokenUtility.createTokenByUser(userVO);
      userVO.setToken(token);
      return userVO;
    }
    return null;
  }

  public void logoffUserToken(String token) {
    tokenUtility.invalidateToken(token);
  }

  public UserVO validateUserToken(String token) {
    Pair<Integer, UserVO> pair = tokenUtility.isTokenValid(token);
    if (pair.getKey() == HttpServletResponse.SC_OK) {
      UserVO user = (UserVO) pair.getValue();
      return user;
    }
    return null;
  }

}
