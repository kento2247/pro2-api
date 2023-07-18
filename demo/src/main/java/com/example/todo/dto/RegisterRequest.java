package com.example.todo.dto;

import lombok.Data;

@Data
public class registerRequest {
    private String nickname;
    private String email;
    private String password;
}
