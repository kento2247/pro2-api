package com.example.todo.dto;

import com.example.todo.model.Task;
import lombok.Data;

import java.util.Set;

@Data
public class getUserResponse {
    private Long id;
    private String nickname;
    private String email;
    private Set<Task> tasks;

}
