package com.br.zallpyws.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class UserVO {
  @Getter @Setter private String email;
  @Getter @Setter private String name;
  @Getter @Setter private String password;
  @Getter @Setter private List<String> profiles;
  @Getter @Setter private List<Integer> idProjects;
  @Getter @Setter private String token;
}
