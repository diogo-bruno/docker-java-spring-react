package com.br.project.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class TimeWorkedVO {
  @Getter @Setter private Date startWork;

  @Getter @Setter private Date endWork;

  @Getter @Setter private Integer projectId;

  @Getter @Setter private Integer userId;
}
