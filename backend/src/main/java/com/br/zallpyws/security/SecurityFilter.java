package com.br.zallpyws.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.br.zallpyws.util.Public;
import com.br.zallpyws.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityFilter implements Filter {

  @Autowired private RequestMappingHandlerMapping requestMappingHandlerMapping;
  @Autowired TokenUtility tokenUtility;

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    if (request.getMethod().equals("OPTIONS")) {
      chain.doFilter(req, res);
    } else {
      if (isSecured(request)) {
        String token = request.getHeader("token");
        if (token == null)
          token = request.getParameter("token");
        Pair<Integer, UserVO> pair = tokenUtility.isTokenValid(token);
        if (pair.getKey() == HttpServletResponse.SC_OK) {
          req.setAttribute("user", pair.getValue());
          chain.doFilter(req, res);
        } else {
          response.sendError(pair.getKey());
        }
      } else {
        chain.doFilter(req, res);
      }
    }
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }

  public boolean isSecured(HttpServletRequest request) {
    try {
      HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
      return handlerExecutionChain != null && handlerExecutionChain.toString().contains("com.br.zallpyws.ws")
          && ((HandlerMethod) handlerExecutionChain.getHandler()).getMethodAnnotation(Public.class) == null;
    } catch (Exception e) {
      log.error("", e);
      return false;
    }
  }

}