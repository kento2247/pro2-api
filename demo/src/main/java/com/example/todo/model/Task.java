package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
    private String title;
    private String body;
    private int priority;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean completed;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean archivedOnCompletion;
    private Date due_date;
    private Date created_at;
    private Date updated_at;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_shared_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> shared_users;

    public void addSharedUser(Users user) {
        if (this.shared_users == null) {
            this.shared_users = new ArrayList<>();
        }
        this.shared_users.add(user);
    }
}
