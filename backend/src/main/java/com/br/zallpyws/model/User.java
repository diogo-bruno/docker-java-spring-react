package com.br.zallpyws.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "user")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_SEQ") @SequenceGenerator(name = "sequenceUser", sequenceName = "USER_SEQ", allocationSize = 1) @Getter @Setter private Integer id;

  @Getter @Setter private String email;
  @Getter @Setter private String name;
  @JsonIgnore @Getter @Setter private String password;

  @Column @ElementCollection(targetClass = String.class) @Getter @Setter private List<String> profiles;

  @Getter @Setter @OneToMany(orphanRemoval = true, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }) @OrderBy("id") private List<Project> projects;

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  public boolean equals(User obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    return new EqualsBuilder().append(id, obj.id).isEquals();
  }

}
