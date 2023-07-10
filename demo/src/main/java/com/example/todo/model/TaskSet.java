package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class TaskSet {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "taskSet", fetch = FetchType.LAZY)
    private List<Task> tasks;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    private String title;
    private String body;
    private Date due_date;
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean is_completed;
    private Date created_at;
    private Date updated_at;
}
