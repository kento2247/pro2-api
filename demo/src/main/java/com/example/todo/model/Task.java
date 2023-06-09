package com.example.todo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Task {

  @Id @GeneratedValue private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "task_set_id")
    private TaskSet taskSet;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "periodic_task_id")
    private PeriodicTask periodicTask;

  private String title;
  private String body;
  private Date due_date;
  private boolean is_completed;
  private Date created_at;
  private Date updated_at;
}
