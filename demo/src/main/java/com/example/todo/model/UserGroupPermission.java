package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class UserGroupPermission {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserGroup userGroup;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_edit;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_create_new_task;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_delete;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_add_user;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_remove_user;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_add_permission;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean can_remove_permission;
    private Date created_at;
    private Date updated_at;
}

