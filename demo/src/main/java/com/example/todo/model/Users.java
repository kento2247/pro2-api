package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Updated
    private Set<Task> tasks;

    public void setCreatedAt(Date createdAt) {
        this.created_at = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updated_at = updatedAt;
    }
}
