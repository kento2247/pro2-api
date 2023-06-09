package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class PeriodicTask {
        @Id
        @GeneratedValue
        private Long id;
        @OneToMany(fetch = FetchType.LAZY)
        private List<Task> tasks;
    }