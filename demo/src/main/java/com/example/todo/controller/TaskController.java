package com.example.todo.controller;

import com.example.todo.dto.NewTaskRequest;
import com.example.todo.jwtUtil.JwtUtil;
import com.example.todo.model.Task;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UsersRepository;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TaskController {

  private final UsersRepository userRepository;
  private final TaskRepository taskRepository;
  private final JwtUtil jwtUtil;

  public TaskController(
      TaskRepository taskRepository, UsersRepository userRepository, JwtUtil jwtUtil) {
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping("/tasks")
  public List<Task> getAllTasksByUserId(@RequestHeader(value = "Authorization") String token) {
    String jwtToken = token.replace("Bearer ", "");
    String email = jwtUtil.extractNickname(jwtToken);
    Users user = userRepository.findByEmail(email);
    return taskRepository.findByUserId(user.getId());
  }

  @GetMapping("/tasks/{id}")
  public Task getTask(@PathVariable Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }

  @PostMapping("/tasks/new")
  public ResponseEntity<Task> createTask(
      @RequestHeader(value = "Authorization") String token,
      @RequestBody NewTaskRequest taskRequest) {
    String jwtToken = token.replace("Bearer ", "");
    String email = jwtUtil.extractNickname(jwtToken);

    Users user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    Task task = new Task();
    task.setTitle(taskRequest.getTitle());
    task.setBody(taskRequest.getBody());
    task.setPriority(taskRequest.getPriority());
    task.set_completed(taskRequest.is_completed());
    task.set_archived_on_completion(taskRequest.is_archived_on_completion());
    task.setDue_date(taskRequest.getDue_date());
    task.setUser(user);

    // Set current date for created_at and updated_at
    Date now = new Date();
    task.setCreated_at(now);
    task.setUpdated_at(now);

    return ResponseEntity.ok(taskRepository.save(task));
  }

  @PutMapping("/tasks/{id}")
  public Task updateTask(
      @AuthenticationPrincipal Users user, @PathVariable Long id, @RequestBody Task taskDetails) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    task.setTitle(taskDetails.getTitle());
    task.setBody(taskDetails.getBody());
    task.setDue_date(taskDetails.getDue_date());
    task.set_completed(taskDetails.is_completed());
    task.setPriority(taskDetails.getPriority());
    task.set_archived_on_completion(taskDetails.is_archived_on_completion());
    return taskRepository.save(task);
  }

  @DeleteMapping("/tasks/{id}")
  public void deleteTask(@PathVariable Long id) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    taskRepository.delete(task);
  }
}
