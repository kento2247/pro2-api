package com.example.todo.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nickname;
    private String email;
    private String password;
}
