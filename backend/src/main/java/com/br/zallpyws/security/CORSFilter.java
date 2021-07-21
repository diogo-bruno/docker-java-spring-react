package com.br.zallpyws.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CORSFilter extends OncePerRequestFilter {

  private @Value("#{'${cors.domains.endswith}'.split(',')}") List<String> dominiosPermitidos;
  private static final String ALLOWED_METHODS = "POST, GET, OPTIONS, DELETE";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String origin = request.getHeader(HttpHeaders.ORIGIN);
    if (origin != null) {
      if (isAllowed(origin)) {
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHODS);
        String reqHead = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (StringUtils.isNotBlank(reqHead))
          response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, reqHead);
      } else {
        return; //interrompe o request retornando 200
      }
    }
    filterChain.doFilter(request, response);
  }

  private boolean isAllowed(String origin) {
    String dominio = UriComponentsBuilder.fromOriginHeader(origin).build().getHost();
    return dominiosPermitidos.stream().anyMatch(dominio::endsWith);
  }

}
