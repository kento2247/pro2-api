package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Task task;
}
