package com.br.project.ws;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.br.project.model.TimeWorked;
import com.br.project.rule.Profile;
import com.br.project.service.TimeWorkedService;
import com.br.project.vo.TimeWorkedVO;

@RestController
public class TimeWorkedWs {

  @Autowired TimeWorkedService timeWorkedService;

  @Profile(Profile.ADM)
  @RequestMapping(value = "/createTimeWorked", method = { RequestMethod.POST })
  public TimeWorked createUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody TimeWorkedVO timeWorkedVO) throws Exception {
    return timeWorkedService.createTimeWorked(timeWorkedVO);
  }

}
