package com.br.zallpyws.ws;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.br.zallpyws.model.TimeWorked;
import com.br.zallpyws.rule.Profile;
import com.br.zallpyws.service.TimeWorkedService;
import com.br.zallpyws.vo.TimeWorkedVO;

@RestController
public class TimeWorkedWs {

  @Autowired TimeWorkedService timeWorkedService;

  @Profile(Profile.ADM)
  @RequestMapping(value = "/createTimeWorked", method = { RequestMethod.POST })
  public TimeWorked createUser(HttpServletRequest request, @RequestHeader HttpHeaders headers, @RequestBody TimeWorkedVO timeWorkedVO) throws Exception {
    return timeWorkedService.createTimeWorked(timeWorkedVO);
  }

}
