package com.example.base.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

  @Id
  @UuidGenerator(style = UuidGenerator.Style.TIME)
  @Column(nullable = false)
  String id;

  @Column(nullable = false, unique = true)
  String username;

  @Column(nullable = false)
  @JsonIgnore
  String password;

  @Column(nullable = false)
  String email;

  @Nationalized
  String firstName;

  @Nationalized
  String lastName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  Role role;
}