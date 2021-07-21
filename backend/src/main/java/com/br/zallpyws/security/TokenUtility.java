package com.br.zallpyws.security;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.br.zallpyws.model.User;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Component
public class TokenUtility {

  @Autowired @Qualifier("cacheDe5Minutos_token") private Cache cache;

  public String createTokenByUser(User user) {
    String simpleTokenRandom = UUID.randomUUID().toString().replaceAll("-", "");
    Element element = new Element(simpleTokenRandom, user);
    cache.put(element);
    return simpleTokenRandom;
  }

  public Pair<Integer, User> isTokenValido(String token, HttpServletRequest request) {
    Element element = cache.get(token);
    if (element != null) {
      User usuario = (User) element.getObjectValue();
      return ImmutablePair.of(HttpServletResponse.SC_OK, usuario);
    } else {
      return ImmutablePair.of(HttpServletResponse.SC_UNAUTHORIZED, null);
    }
  }

}
