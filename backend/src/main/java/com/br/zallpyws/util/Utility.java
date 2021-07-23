package com.br.zallpyws.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Utility {

  public static String concatenarString(String joiner, String... elements) {
    StringJoiner strJoiner = new StringJoiner(joiner);
    for (int i = 0; i < elements.length; i++)
      strJoiner.add(elements[i]);
    return strJoiner.toString();
  }

  public static String addXForwardedFor(HttpServletRequest httpServletRequest) {
    String xff = httpServletRequest.getHeader("X-FORWARDED-FOR");
    String remoteAddress = httpServletRequest.getRemoteAddr();
    if (xff == null || xff.isEmpty()) {
      return remoteAddress;
    } else {
      return xff + "," + remoteAddress;
    }
  }

  public static String upperCaseTrim(String str) {
    return str == null ? null : str.toUpperCase().trim();
  }

  public static Date addHour(Date data, Integer qtdHoras) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(data);
    cal.add(Calendar.HOUR, qtdHoras);
    return cal.getTime();
  }

  public static Date formatToDateBnmp(String string) {
    if (string != null) {
      try {
        return new SimpleDateFormat("yyyy-MM-dd").parse(string.toString());
      } catch (Exception e) {
        return new Date(Long.valueOf(string.toString()));
      }
    } else {
      return null;
    }
  }

  public static String generateBCryptHash(String str) {
    return BCrypt.hashpw(str, BCrypt.gensalt());
  }

  public static boolean checkBCrypt(String passCheck, String pass2Check) {
    try {
      return BCrypt.checkpw(passCheck, pass2Check);
    } catch (Exception e) {
      throw new CustomException("HOUVE UM ERRO AO LOGAR, VERIFIQUE SE OS DADOS FORAM INFORMADOS CORRETAMENTE, SE NECESSÁRIO CLIQUE EM 'RECUPERAR SENHA'.");
    }
  }

}
