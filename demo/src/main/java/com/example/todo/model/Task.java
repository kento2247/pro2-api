package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne // Updated
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users user;
    private String title;
    private String body;
    private int priority;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean is_completed;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean is_archived_on_completion;
    private Date due_date;
    private Date created_at;
    private Date updated_at;
}
