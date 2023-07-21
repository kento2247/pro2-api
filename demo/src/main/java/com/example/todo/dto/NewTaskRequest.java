package com.example.todo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NewTaskRequest {
    private String title;
    private String body;
    private int priority;
    private boolean is_completed;
    private boolean is_archived_on_completion;
    private Date due_date;
    private Date created_at;
    private Date updated_at;
    private long[] shared_users;
}
