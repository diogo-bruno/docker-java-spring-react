package com.br.zallpyws.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "time_worked")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TimeWorked implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "TIME_PROJECT_SEQ") @SequenceGenerator(name = "sequenceTimesProjects", sequenceName = "TIME_PROJECT_SEQ", allocationSize = 1) @Getter @Setter private Integer id;

  @Getter @Setter private Date startWork;

  @Getter @Setter private Date endWork;

  @Getter @Setter @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }) @OrderBy("id") private Project project;

  @Getter @Setter @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }) @OrderBy("id") private User user;

}