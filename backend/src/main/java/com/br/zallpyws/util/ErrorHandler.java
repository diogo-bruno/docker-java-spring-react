package com.br.zallpyws.util;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class ErrorHandler extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
    Throwable error = super.getError(webRequest);
    if (error instanceof CustomException) {
      CustomException ae = (CustomException) error;
      errorAttributes.put("messages", ae.getMessages());
      errorAttributes.put("msg", ae.getMessages());
    } else if (error != null) {
      errorAttributes.put("messages", Arrays.asList(error.getMessage()));
      errorAttributes.put("msg", Arrays.asList(error.getMessage()));
    }
    return errorAttributes;
  }

}
