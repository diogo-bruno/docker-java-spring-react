package com.br.zallpyws.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;

@Aspect
@Component
public class ProfileAspect {

  @Around("execution(@com.br.zallpyws.rule.Profile * *(..)) && @annotation(profile)")
  public Object ignore(ProceedingJoinPoint joinPoint, Profile profile) throws Throwable {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    JsonNode usuario = (JsonNode) request.getAttribute("usuario");

    String[] perfisUsuario = getPerfisUsuario(usuario);
    String[] perfisAnotados = profile.value();

    if (!hasPermission(perfisUsuario, perfisAnotados)) {
      HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
          String.format("Seus perfis %s não podem acessar esse método. Requer %s.", Arrays.toString(perfisUsuario), Arrays.toString(perfisAnotados)));
      return null;
    }

    return joinPoint.proceed();
  }

  private boolean hasPermission(String[] perfisUsuario, String[] perfisAnotados) {
    for (String perfilUsuario : perfisUsuario)
      for (String perfilAnotado : perfisAnotados)
        if (perfilUsuario.equals(perfilAnotado))
          return true;
    return false;
  }

  private String[] getPerfisUsuario(JsonNode usuario) {
    List<String> list = new ArrayList<String>();
    JsonNode perfis = usuario.get("perfis");
    if (perfis != null) {
      for (JsonNode perfil : perfis) {
        list.add(perfil.get("descricao").asText().toString().toUpperCase());
      }
    }
    return list.toArray(new String[0]);
  }

}
