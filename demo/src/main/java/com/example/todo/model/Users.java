package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Users {

   @Id
   @GeneratedValue
   private Long id;
   @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserGroupPermission> userGroupPermission;

   private String first_name;
   private String last_name;
   private String email;
   private String password;
   private String nickname;
   private String status;


}
