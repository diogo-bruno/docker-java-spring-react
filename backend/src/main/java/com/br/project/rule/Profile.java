package com.br.project.rule;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Profile {

  public static String ADM = "ADM";
  public static String PROGRAMADOR = "PROGRAMADOR";

  String[] value() default {};

}
