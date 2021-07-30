package com.br.project.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Getter private List<String> messages = new ArrayList<String>();

  public CustomException() {

  }

  public CustomException(String msg) {
    super(msg);
    messages.add(msg);
  }

  public CustomException(String msg, Throwable t) {
    super(msg, t);
    messages.add(msg);
  }

  public CustomException(List<String> messages) {
    this.messages = messages;
  }

  public void addMessage(String message) {
    messages.add(message);
    message = Arrays.toString(messages.toArray());
  }

}