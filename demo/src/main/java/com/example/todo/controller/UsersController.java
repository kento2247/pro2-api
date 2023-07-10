package com.example.todo.controller;

import com.example.todo.jwtUtil.JwtUtil;
import com.example.todo.model.Users;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsersController(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/users/{userId}")
    public Users getUser(@PathVariable Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("users/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        usersRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("users/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        Users existingUser = usersRepository.findByEmail(user.getEmail());
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            String jwt = jwtUtil.generateToken(existingUser.getEmail());
            return ResponseEntity.ok(jwt);
        } else {
            throw new RuntimeException("invalid login details");
        }

    }

    @PatchMapping("/users/{userId}")
    public Users updateUser(@PathVariable Long userId, @RequestBody Users user) {
        Users userToUpdate = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        userToUpdate.setFirst_name(user.getFirst_name());
        userToUpdate.setLast_name(user.getLast_name());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());

        return usersRepository.save(userToUpdate);
    }

}

