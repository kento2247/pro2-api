package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class UserGroup {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany(mappedBy = "groups")
    private List<Users> users;
    @OneToMany(mappedBy = "userGroup", fetch = FetchType.LAZY)
    private List<TaskSet> taskSet;
    @OneToMany(fetch = FetchType.LAZY)
    private List<UserGroupPermission> userGroupPermission;

    private String name;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean is_personal;
    private Date created_at;
    private Date updated_at;
}
