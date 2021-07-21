package com.br.zallpyws.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CustomHealthCheck {

  private @Autowired HealthEndpoint he;
  private boolean readnessFirst200 = false;
  private String readnessMsg = "Opera como se fosse o /health somente uma vez. Depois retorna 200 sempre.";
  private String livenessMsg = "Um simples http que retorna 200";

  @RequestMapping(value = "/readness", method = { RequestMethod.GET })
  public ResponseEntity<String> readness(HttpServletRequest httpServletRequest) {
    if (!readnessFirst200) {
      HealthComponent health = he.health();
      if (health.getStatus() == Status.UP) {
        readnessFirst200 = true;
      } else {
        log.error(health.toString()); //manda pro kibana o motivo do != 200
        return ResponseEntity.status(999).body("Status: 999 - " + readnessMsg);
      }
    }
    return ResponseEntity.ok().body("Status: 200 - " + readnessMsg);
  }

  @RequestMapping(value = "/liveness", method = { RequestMethod.GET })
  public ResponseEntity<String> liveness() {
    return ResponseEntity.ok().body("Status: 200 - " + livenessMsg);
  }

}
