package com.example.todo.controller;

import com.example.todo.dto.LoginRequest;
import com.example.todo.dto.RegisterRequest;
import com.example.todo.dto.getUserResponse;
import com.example.todo.jwtUtil.JwtUtil;
import com.example.todo.model.Users;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    @GetMapping("users")
    public getUserResponse getAllUser() {
        getUserResponse response = new getUserResponse();
        Users user = usersRepository.findAll().get(0);
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setTasks(user.getTasks());
        return response;
    }

    @GetMapping("users/self")
    public getUserResponse getUser(@RequestHeader(value = "Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        System.out.println(jwtToken);
        String email = jwtUtil.extractNickname(jwtToken);
        Users user = usersRepository.findByEmail(email);

        getUserResponse response = new getUserResponse();
        response.setId(user.getId());
        response.setNickname(user.getNickname());
        response.setEmail(user.getEmail());
        response.setEmail(user.getEmail());
        response.setTasks(user.getTasks());
        return response;
    }

    @PostMapping("users/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest userRequest) {
        Users existingUser = usersRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }
        Users user = new Users();
        user.setNickname(userRequest.getNickname());
        user.setEmail(userRequest.getEmail());
        user.setNickname(userRequest.getNickname());
        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(hashedPassword);

        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        usersRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest user) {
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

        userToUpdate.setNickname(user.getNickname());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());

        return usersRepository.save(userToUpdate);
    }

}

