package com.br.zallpyws.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Project implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "PROJECT_SEQ") @SequenceGenerator(name = "sequenceProjects", sequenceName = "PROJECT_SEQ", allocationSize = 1) @Getter @Setter private Integer id;

  @Getter @Setter private String description;

  @Getter @Setter @Transient long totalMinutesWorked;

  @Getter @Setter @Transient long totalWorkers;

}
