package com.br.project.rule;

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

import com.br.project.vo.UserVO;

@Aspect
@Component
public class ProfileAspect {

  @Around("execution(@com.br.project.rule.Profile * *(..)) && @annotation(profile)")
  public Object ignore(ProceedingJoinPoint joinPoint, Profile profile) throws Throwable {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    UserVO user = (UserVO) request.getAttribute("user");

    String[] profilesUser = getProfileUser(user);
    String[] profileNoted = profile.value();

    if (!hasPermission(profilesUser, profileNoted)) {
      HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String.format("Seus perfis %s não podem acessar esse método. Requer %s.", Arrays.toString(profilesUser), Arrays.toString(profilesUser)));
      return null;
    }

    return joinPoint.proceed();
  }

  private boolean hasPermission(String[] profilesUser, String[] perfisAnotados) {
    for (String profileUser : profilesUser)
      for (String perfilNoted : perfisAnotados)
        if (profileUser.equals(perfilNoted))
          return true;
    return false;
  }

  private String[] getProfileUser(UserVO user) {
    List<String> list = new ArrayList<String>();
    List<String> profiles = user.getProfiles();
    if (profiles != null) {
      for (String profile : profiles) {
        list.add(profile.toUpperCase());
      }
    }
    return list.toArray(new String[0]);
  }

}
