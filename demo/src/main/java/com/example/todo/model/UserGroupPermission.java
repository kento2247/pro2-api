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

        private boolean can_edit;
        private boolean can_create_new_task;
        private boolean can_delete;
        private boolean can_add_user;
        private boolean can_remove_user;
        private boolean can_add_permission;
        private boolean can_remove_permission;
        private Date created_at;
        private Date updated_at;
}

