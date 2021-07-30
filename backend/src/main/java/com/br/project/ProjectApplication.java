package com.br.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class ProjectApplication {
  public static void main(String[] args) {
    //RelaxSecurity.allowAllCertificatesHTTPS();

    try {
      SpringApplication.run(ProjectApplication.class, args);
    } catch (Throwable e) {
      if (e.getClass().getName().contains("SilentExitException")) {
        log.debug("Spring is restarting the main thread - See spring-boot-devtools");
      } else {
        log.error("Application crashed!", e);
      }
    }

  }

}
