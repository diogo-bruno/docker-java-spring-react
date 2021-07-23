
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.zallpyws.model.User;
import com.br.zallpyws.rule.Profile;
import com.br.zallpyws.service.UserService;
import com.br.zallpyws.util.Public;
import com.br.zallpyws.vo.LoginVO;
import com.br.zallpyws.vo.UserVO;

@RestController
public class UserWs {

  @Autowired UserService userService;

  @Public
  @RequestMapping(value = "/login", method = { RequestMethod.POST })
  public UserVO login(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody LoginVO loginVO) throws Exception {
    return userService.getUserLogin(loginVO.getEmail(), loginVO.getPassword());
  }

  @Public
  @RequestMapping(value = "/logoff", method = { RequestMethod.GET })
  public void logoff(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestParam("token") String token) throws Exception {
    userService.logoffUserToken(token);
  }

  @Public
  @RequestMapping(value = "/validate", method = { RequestMethod.GET })
  public UserVO validate(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestParam("token") String token) throws Exception {
    return userService.validateUserToken(token);
  }

  @Profile(Profile.ADM)
  @RequestMapping(value = "/createUser", method = { RequestMethod.POST })
  public User createUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody UserVO userVO) throws Exception {
    return userService.createUser(userVO);
  }

  @Profile(Profile.ADM)
  @RequestMapping(value = "/listAllUsers", method = { RequestMethod.GET })
  public List<User> listAllUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("id") Integer id) throws Exception {
    return userService.listAllUser();
  }

  @Profile(Profile.ADM)
  @RequestMapping(value = "/listAllUsersAndTimeWorked", method = { RequestMethod.GET })
  public List<User> listAllUsersAndTimeWorked(HttpServletRequest request, @RequestHeader HttpHeaders headers) throws Exception {
    return userService.listAllUsersAndTimeWorked();
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/getUser/{userId}", method = { RequestMethod.GET })
  public User getUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("userId") Integer userId) throws Exception {
    return userService.getUser(userId);
  }

  @Profile({ Profile.ADM, Profile.PROGRAMADOR })
  @RequestMapping(value = "/getProjectsAndTimeWorkedByUser/{userId}", method = { RequestMethod.GET })
  public User getProjectsAndTimeWorkedByUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @PathVariable("userId") Integer userId) throws Exception {
    return userService.getProjectsAndTimeWorkedByUser(userId);
  }

}
