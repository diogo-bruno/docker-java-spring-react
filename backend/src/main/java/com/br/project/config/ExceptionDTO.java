package com.br.project.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ExceptionDTO {

  @Getter @Setter private Integer status;

  @Getter @Setter private String error;
  @Getter @Setter private String message;
  @Getter private List<String> messages = new ArrayList<String>();
  @Getter @Setter private String timeStamp;
  //  @Getter @Setter private String trace;

  public ExceptionDTO(List<String> messages) {
    this.messages = messages;
  }

  public ExceptionDTO(String message, List<String> messages) {
    this.message = message;
    this.messages = messages;
  }

}
